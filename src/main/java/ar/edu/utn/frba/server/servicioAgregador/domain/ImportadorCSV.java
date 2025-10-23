package ar.edu.utn.frba.server.servicioAgregador.domain;

import ar.edu.utn.frba.server.servicioAgregador.domain.Etiqueta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Importador CSV robusto para crear Hechos a partir de un archivo con separador ';'.
 *
 * Reglas admitidas:
 *  - Encabezados tolerantes a espacios y mayúsculas/minúsculas.
 *  - Columnas esperadas: "Titulo", "Descripcion" (o "Descripción"), "Categoria",
 *    "latitud", "longitud", "fecha del hecho" (o "fecha").
 *  - Formatos de fecha admitidos: dd/MM/yyyy, yyyy-MM-dd.
 *  - Manejo por-fila con logs: errores en una fila NO abortan todo el import.
 *  - Búsqueda del archivo: primero classpath (resources), luego filesystem absoluto/relativo.
 */

@Component("importadorCSVAgregador")
public class ImportadorCSV {
    private static final Logger log = LoggerFactory.getLogger(ImportadorCSV.class);
    private static final char DEFAULT_SEPARATOR = ';';

    private static final Charset[] CHARSETS_TRY = new Charset[]{
            StandardCharsets.UTF_8,
            StandardCharsets.ISO_8859_1
    };

    private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ISO_LOCAL_DATE // yyyy-MM-dd
    );

    /**
     * Importa hechos desde un archivo CSV ubicado en classpath o filesystem.
     */

    public List<Hecho> importar(String pathOrResource) {
        if (pathOrResource == null) { log.warn("[CSV] Path nulo"); return List.of(); }

        String path = pathOrResource.trim().replaceAll("^\"|\"$", "");
        if (path.startsWith("classpath:")) path = path.substring("classpath:".length());

        // 1) classpath
        try {
            Resource res = new ClassPathResource(path);
            if (res.exists()) {
                log.info("[CSV] Leyendo desde classpath: {}", path);
                return leer(res.getInputStream());
            }
        } catch (IOException e) {
            log.debug("[CSV] Error leyendo desde classpath {}: {}", path, e.toString());
        }

        // 2) filesystem
        File f = new File(path);
        if (!f.exists()) f = new File("./" + path);
        if (f.exists() && f.isFile()) {
            log.info("[CSV] Leyendo desde filesystem: {}", f.getAbsolutePath());
            for (Charset cs : CHARSETS_TRY) {
                try (InputStream in = new FileInputStream(f)) {
                    return leer(in, cs);
                } catch (IOException ex) {
                    log.debug("[CSV] Error con charset {}: {}", cs, ex.toString());
                }
            }
        } else {
            log.warn("[CSV] No se encontró el archivo en classpath ni filesystem: {}", path);
        }
        return List.of();
    }

    /** --- Implementación --- */

    private List<Hecho> leer(InputStream in) {
        return leer(in, StandardCharsets.UTF_8);
    }

    private List<Hecho> leer(InputStream in, Charset cs) {
        List<Hecho> hechos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, cs))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                log.warn("[CSV] Archivo vacío");
                return List.of();
            }

            List<String> headers = parseLine(headerLine, DEFAULT_SEPARATOR);
            Map<String, Integer> idx = indexar(headers);

            String line;
            int rowNum = 1; // ya leímos encabezado
            while ((line = br.readLine()) != null) {
                rowNum++;
                if (line.isBlank()) continue;
                List<String> cols = parseLine(line, DEFAULT_SEPARATOR);
                try {
                    Hecho h = mapear(cols, idx, rowNum);
                    if (h != null) hechos.add(h);
                } catch (Exception e) {
                    log.warn("[CSV] Fila {} inválida: {} | line='{}'", rowNum, e.getMessage(), line);
                }
            }
        } catch (IOException e) {
            log.error("[CSV] Error general leyendo CSV: {}", e.toString());
        }
        return hechos;
    }

    private Map<String, Integer> indexar(List<String> headers) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            String key = norm(headers.get(i));
            map.put(key, i);
        }
        // Aliases útiles
        alias(map, "descripcion", "descripción");
        alias(map, "fecha del hecho", "fecha");
        return map;
    }

    private void alias(Map<String, Integer> map, String canonical, String alt) {
        if (!map.containsKey(canonical) && map.containsKey(alt)) {
            map.put(canonical, map.get(alt));
        }
    }

    private Hecho mapear(List<String> cols, Map<String, Integer> idx, int rowNum) {
        String titulo = get(cols, idx, "titulo");
        String descripcion = get(cols, idx, "descripcion");
        String categoria = get(cols, idx, "categoria");
        String latStr = get(cols, idx, "latitud");
        String lonStr = get(cols, idx, "longitud");
        String fechaStr = get(cols, idx, "fecha del hecho");

        if (isAllBlank(titulo, descripcion, categoria, latStr, lonStr, fechaStr)) return null;

        Hecho h = new Hecho();
        h.setTitulo(titulo == null ? null : titulo.trim());
        h.setDescripcion(nullIfBlank(descripcion));
        h.setCategoria(nullIfBlank(categoria));
        h.setLatitud(parseDouble(latStr));
        h.setLongitud(parseDouble(lonStr));
        h.setFechaAcontecimiento(parseFecha(fechaStr));
        h.setFechaCarga(java.time.LocalDate.now());
        h.setEstadoRevision(ar.edu.utn.frba.server.common.enums.EstadoRevisionHecho.APROBADO);
        h.setFuentes(List.of(new ar.edu.utn.frba.server.servicioAgregador.domain.Fuente(
                ar.edu.utn.frba.server.common.enums.TipoFuente.ESTATICA
        )));
        String provincia = get(cols, idx, "provincia");
        h.setProvincia(provincia != null && !provincia.isBlank()
                ? provincia.trim()
                : ResolverProvinciaDesdeLatLon(h.getLatitud(), h.getLongitud()));

        return h;
    }

    private String get(List<String> cols, Map<String, Integer> idx, String key) {
        Integer i = idx.get(key);
        if (i == null || i < 0 || i >= cols.size()) return null;
        String v = cols.get(i);
        return v == null ? null : v.trim();
    }

    private boolean isAllBlank(String... xs) {
        for (String x : xs) if (x != null && !x.isBlank()) return false;
        return true;
    }

    private String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private Double parseDouble(String s) {
        if (s == null || s.isBlank()) return null;
        // tolerar coma decimal
        String n = s.replace(',', '.');
        try {
            return Double.parseDouble(n);
        } catch (NumberFormatException e) {
            log.warn("[CSV] no se pudo parsear número '{}': {}", s, e.getMessage());
            return null;
        }
    }

    private LocalDate parseFecha(String s) {
        if (s == null || s.isBlank()) return null;
        for (DateTimeFormatter f : DATE_FORMATS) {
            try {
                return LocalDate.parse(s, f);
            } catch (DateTimeParseException ignored) { }
        }
        // intentos extra: normalizar separadores
        String s2 = s.replace('.', '/').replace('-', '/');
        for (DateTimeFormatter f : DATE_FORMATS) {
            try {
                return LocalDate.parse(s2, f);
            } catch (DateTimeParseException ignored) { }
        }
        log.warn("[CSV] fecha inválida '{}'. Formatos soportados: dd/MM/yyyy, yyyy-MM-dd", s);
        return null;
    }

    /**
     * Parser simple para CSV con separador ';' y soporte básico de comillas dobles.
     * Si tu CSV contiene comas dentro de campos y comillas, esto es suficiente. Para casos
     * más complejos, considera usar una librería (OpenCSV) y adaptar este importador.
     */
    private List<String> parseLine(String line, char sep) {
        if (line == null) return List.of();
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                // toggle o escape de ""
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++; // saltar el segundo
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == sep && !inQuotes) {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        // trim suave
        return out.stream().map(s -> s == null ? null : s.trim()).collect(Collectors.toList());
    }

    public Hecho parsearLineaCSV(String line) {
        if (line == null || line.trim().isEmpty()) return null;

        final String CSV_DELIMITER = ";";
        String[] values = line.trim().split(CSV_DELIMITER);

        // Ajustá si cambia tu layout (7 columnas mínimas)
        if (values.length < 7) return null;

        String titulo = values[0].trim();
        String descripcion = values[1].trim();
        String categoria = values[2].trim();

        Double latitud = null;
        if (!values[4].trim().isEmpty()) {
            latitud = Double.parseDouble(values[4].trim().replace(',', '.'));
        }

        Double longitud = null;
        if (!values[5].trim().isEmpty()) {
            longitud = Double.parseDouble(values[5].trim().replace(',', '.'));
        }

        java.time.LocalDate fechaAcontecimiento = null;
        if (!values[6].trim().isEmpty()) {
            // usa tu formateador si lo necesitás
            fechaAcontecimiento = java.time.LocalDate.parse(values[6].trim(), java.time.format.DateTimeFormatter.ofPattern("d/M/yyyy"));
        }

        List<Etiqueta> etiquetas = new ArrayList<>();
        if (values.length > 7 && !values[7].trim().isEmpty() && !"null".equalsIgnoreCase(values[7].trim())) {
            for (String e : values[7].trim().split(",")) etiquetas.add(new Etiqueta(e.trim()));
        }

        // id aleatorio positivo
        long idHecho = new java.security.SecureRandom().nextLong();
        if (idHecho <= 0) idHecho = Math.abs(idHecho);

        Hecho h = new Hecho();

        h.setTitulo(titulo);
        h.setDescripcion(descripcion);
        h.setCategoria(categoria);
        h.setContenidoMultimedia(null);
        h.setLatitud(latitud);
        h.setLongitud(longitud);
        h.setFechaAcontecimiento(fechaAcontecimiento);
        h.setFechaCarga(java.time.LocalDate.now());
        h.setEtiquetas(etiquetas);
        h.setEstadoRevision(ar.edu.utn.frba.server.common.enums.EstadoRevisionHecho.APROBADO);
        h.setFuentes(List.of(new ar.edu.utn.frba.server.servicioAgregador.domain.Fuente(
                ar.edu.utn.frba.server.common.enums.TipoFuente.ESTATICA
        )));

        return h;

    }

    private String norm(String s) {
        if (s == null) return "";
        return s.trim().toLowerCase(Locale.ROOT);
    }

    public List<Hecho> importarDesdeClasspath(String resourcePath) {
        try {
            var res = new ClassPathResource(resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath);
            try (var in = res.getInputStream();
                 var br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                return leer(in, StandardCharsets.UTF_8); // o un "parsear(br)" si lo tenés
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el recurso '" + resourcePath + "'", e);
        }
    }

    private String ResolverProvinciaDesdeLatLon(Double lat, Double lon) {
        if (lat == null || lon == null) return "Desconocida";
        if (lat < -30 && lon < -60) return "Buenos Aires";
        if (lat < -27 && lon >= -60) return "Santa Fe";
        if (lat < -24) return "Córdoba";
        return "Desconocida";
    }
}
