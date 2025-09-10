package ar.edu.utn.frba.Server.Servicio_Agregador.Domain;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/** Importador CSV: si no hay columna -> campo queda null. */
@Component("importadorColeccionesCsv")
public class ImportadorCSV {

  private static final Charset[] CHARSETS_TRY = new Charset[]{
          StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1
  };

  private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
          DateTimeFormatter.ofPattern("d/M/yyyy"),
          DateTimeFormatter.ofPattern("dd/MM/yyyy"),
          DateTimeFormatter.ISO_LOCAL_DATE,             // yyyy-MM-dd
          DateTimeFormatter.ofPattern("MM/dd/yyyy"),
          DateTimeFormatter.ofPattern("d-M-yyyy"),
          DateTimeFormatter.ofPattern("dd-MM-yyyy")
  );

  public List<Hecho> importar(String pathOrResource) {
    if (pathOrResource == null || pathOrResource.isBlank()) return List.of();

    String normalized = pathOrResource.strip();
    if (normalized.startsWith("/")) normalized = normalized.substring(1);

    // 1) classpath
    try {
      Resource res = new ClassPathResource(normalized);
      if (res.exists()) try (InputStream in = res.getInputStream()) { return leer(in, StandardCharsets.UTF_8); }
    } catch (IOException ignore) {}

    // 2) filesystem
    File f = new File(pathOrResource);
    if (!f.exists()) f = new File("./" + pathOrResource);
    if (f.exists() && f.isFile()) {
      for (Charset cs : CHARSETS_TRY) {
        try (InputStream in = new FileInputStream(f)) { return leer(in, cs); }
        catch (IOException ignore) {}
      }
    }

    // 3) fallback
    try {
      Resource res = new ClassPathResource("archivodefinitivo.csv");
      if (res.exists()) try (InputStream in = res.getInputStream()) { return leer(in, StandardCharsets.UTF_8); }
    } catch (IOException ignore) {}

    return List.of();
  }

  // ---------- lectura ----------
  private List<Hecho> leer(InputStream in, Charset cs) throws IOException {
    List<Hecho> hechos = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(in, cs))) {
      String headerLine = stripBOM(br.readLine());
      if (headerLine == null) return List.of();

      char sep = detectSeparator(headerLine);
      List<String> headers = parseLine(headerLine, sep);
      Map<String, Integer> idx = indexar(headers);

      String line;
      while ((line = br.readLine()) != null) {
        if (line.isBlank()) continue;
        List<String> cols = parseLine(line, sep);
        Hecho h = map(cols, idx);
        if (h != null) hechos.add(h);
      }
    }
    return hechos;
  }

  private String stripBOM(String s) { return (s != null && s.startsWith("\uFEFF")) ? s.substring(1) : s; }
  private char detectSeparator(String header) {
    int sc = count(header, ';'), cc = count(header, ','), tc = count(header, '\t');
    if (sc >= cc && sc >= tc && sc > 0) return ';';
    if (cc >= sc && cc >= tc && cc > 0) return ',';
    if (tc > 0) return '\t';
    return ';';
  }
  private int count(String s, char c) { int n=0; for (int i=0;i<s.length();i++) if (s.charAt(i)==c) n++; return n; }

  private Map<String,Integer> indexar(List<String> headers) {
    Map<String,Integer> map = new HashMap<>();
    for (int i=0;i<headers.size();i++) map.put(canon(headers.get(i)), i);

    // básicos
    alias(map, "titulo", "title", "nombre");
    alias(map, "descripcion", "descripción", "description", "detalle");
    alias(map, "categoria", "categoría", "category");

    // geo
    alias(map, "latitud", "lat", "latitude");
    alias(map, "longitud", "lon", "lng", "long", "longitude");

    // fechas / hora
    alias(map, "fechaacontecimiento", "fecha", "fecha_del_hecho", "fechadelhecho", "fecha_hecho", "date");
    alias(map, "fechacarga", "fecha_carga", "fechadeingreso", "ingreso", "date_load");
    alias(map, "hora", "hour", "hora_del_dia", "time", "horadelhecho", "hs");

    // provincia
    alias(map, "provincia", "province", "estado", "provincia_iso", "provincia(iso)");

    // fuente (tipo)
    alias(map, "fuente", "source");
    alias(map, "tipofuente", "tipo_fuente", "source_type", "tipo");

    return map;
  }
  private void alias(Map<String,Integer> map, String canonical, String... alts) {
    for (String a : alts) {
      String ca = canon(a);
      if (!map.containsKey(canonical) && map.containsKey(ca)) map.put(canonical, map.get(ca));
    }
  }
  private String canon(String s) {
    if (s == null) return "";
    String t = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
            .replaceAll("\\p{M}+", "");   // quita tildes
    return t.toLowerCase(Locale.ROOT).replaceAll("[\\s_]+",""); // quita espacios/guiones bajos
  }

  private Hecho map(List<String> cols, Map<String,Integer> idx) {
    String titulo       = get(cols, idx, "titulo");
    String descripcion  = get(cols, idx, "descripcion");
    String categoria    = get(cols, idx, "categoria");
    Double lat          = parseDouble(get(cols, idx, "latitud"));
    Double lon          = parseDouble(get(cols, idx, "longitud"));
    LocalDate fecha     = parseFecha(get(cols, idx, "fechaacontecimiento"));
    LocalDate fechaCarga= parseFecha(get(cols, idx, "fechacarga"));
    Integer hora        = parseHora(get(cols, idx, "hora"));
    String provincia    = get(cols, idx, "provincia");
    String fuenteStr    = get(cols, idx, "tipofuente");
    if (fuenteStr == null) fuenteStr = get(cols, idx, "fuente"); // por si usan solo "fuente"

    boolean todoVacio =
            isBlank(titulo) && isBlank(descripcion) && isBlank(categoria) &&
                    lat == null && lon == null && fecha == null && fechaCarga == null &&
                    hora == null && isBlank(provincia) && isBlank(fuenteStr);

    if (todoVacio) return null;

    Hecho h = new Hecho();
    h.setTitulo(nz(titulo));
    h.setDescripcion(nz(descripcion));
    h.setCategoria(nz(categoria));
    h.setLatitud(lat);
    h.setLongitud(lon);
    h.setFechaAcontecimiento(fecha);
    // —— estos 4 son los que mencionaste —— //
    h.setFechaCarga(fechaCarga);                      // null si no vino
    h.setProvincia(nz(provincia));                    // null si no vino
    h.setHora(hora);                                  // null si no vino
    h.setFuente(parseTipoFuente(fuenteStr));          // null si no vino / no matchea
    return h;
  }

  private String get(List<String> cols, Map<String,Integer> idx, String key) {
    Integer i = idx.get(key);
    if (i == null || i < 0 || i >= cols.size()) return null;
    String v = cols.get(i);
    return v == null ? null : v.trim();
  }

  private boolean isBlank(String s) { return s == null || s.isBlank(); }
  private String nz(String s) { return isBlank(s) ? null : s; }

  private Double parseDouble(String s) {
    if (isBlank(s)) return null;
    try { return Double.parseDouble(s.replace(',', '.')); }
    catch (NumberFormatException e) { return null; }
  }
  private LocalDate parseFecha(String s) {
    if (isBlank(s)) return null;
    String v = s.trim().replace('.', '/').replace('-', '/');
    for (DateTimeFormatter f : DATE_FORMATS) {
      try { return LocalDate.parse(v, f); } catch (DateTimeParseException ignore) {}
    }
    // intento swap d/m -> m/d
    String[] p = v.split("/");
    if (p.length == 3) {
      String swapped = p[1] + "/" + p[0] + "/" + p[2];
      for (DateTimeFormatter f : DATE_FORMATS) {
        try { return LocalDate.parse(swapped, f); } catch (DateTimeParseException ignore) {}
      }
    }
    return null;
  }
  private Integer parseHora(String s) {
    if (isBlank(s)) return null;
    try {
      String t = s.trim().toLowerCase(Locale.ROOT).replace("hs","").replace("h","").trim();
      int hh;
      if (t.contains(":")) hh = Integer.parseInt(t.split(":",2)[0].trim());
      else hh = Integer.parseInt(t);
      return Math.max(0, Math.min(23, hh));
    } catch (NumberFormatException e) { return null; }
  }
  private TipoFuente parseTipoFuente(String s) {
    if (isBlank(s)) return null;
    String t = s.trim().toUpperCase(Locale.ROOT);
    // intenta valueOf directo
    try { return TipoFuente.valueOf(t); } catch (Exception ignore) {}
    // alias comunes
    return switch (t) {
      case "CSV" -> TipoFuente.LOCAL;
      case "API", "PROXY", "SERVICIO", "SERVICE" -> TipoFuente.PROXY;
      default -> null; // desconocido -> null (como pediste)
    };
  }

  // CSV parser con comillas
  private List<String> parseLine(String line, char sep) {
    ArrayList<String> out = new ArrayList<>();
    StringBuilder cur = new StringBuilder();
    boolean inQ = false;
    for (int i=0;i<line.length();i++) {
      char c = line.charAt(i);
      if (c == '"') {
        if (inQ && i+1<line.length() && line.charAt(i+1) == '"') { cur.append('"'); i++; }
        else inQ = !inQ;
      } else if (c == sep && !inQ) { out.add(cur.toString()); cur.setLength(0); }
      else cur.append(c);
    }
    out.add(cur.toString());
    for (int i=0;i<out.size();i++) out.set(i, out.get(i)==null? null : out.get(i).trim());
    return out;
  }
}
