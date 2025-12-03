package ar.edu.utn.frba.server.fuente.estatica.services;

import ar.edu.utn.frba.server.fuente.estatica.domain.ImportadorCSV;
import ar.edu.utn.frba.server.fuente.estatica.repositories.IHechosEstaticosRepository;
import ar.edu.utn.frba.server.servicioAgregador.domain.ContenidoMultimedia;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class FuenteEstaticaService implements IFuenteEstaticaService {

    private final ImportadorCSV importador;
    private final EntityManager em;

    @Autowired
    private final IHechosEstaticosRepository repositorio;

    public FuenteEstaticaService(ImportadorCSV importador, EntityManager em, IHechosEstaticosRepository repositorio) {
        this.importador = importador;
        this.em = em;
        this.repositorio = repositorio;
    }

    @Transactional()
    public List<Hecho> obtenerTodosHechos() {
        List<Hecho> hechos = repositorio.findAllFromHecho();

        return hechos.stream()
                .map(h -> Hecho.builder()
                        .idHecho(h.getIdHecho())
                        .titulo(h.getTitulo())
                        .descripcion(h.getDescripcion())
                        .categoria(h.getCategoria())
                        .provincia(h.getProvincia())
                        .latitud(h.getLatitud())
                        .longitud(h.getLongitud())
                        .fechaAcontecimiento(h.getFechaAcontecimiento())
                        .horaAcontecimiento(h.getHoraAcontecimiento())
                        .fechaCarga(h.getFechaCarga())
                        .consensuado(Boolean.TRUE.equals(h.getConsensuado()))
                        .eliminado(h.isEliminado()).build())
                .toList();
    }

    @Override
    public List<Hecho> sincronizar() {
        return List.of();
    }


    @Transactional
    public List<Hecho> importarHechos(String path) {
        em.setFlushMode(FlushModeType.COMMIT); // No flush automático

        List<Hecho> nuevosHechos = importador.importar(path);

        // 1. limpiar ids como antes
        for (Hecho h : nuevosHechos) {
            h.setIdHecho(null);

            if (h.getContenidoMultimedia() != null) {
                try {
                    var idField = h.getContenidoMultimedia().getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(h.getContenidoMultimedia(), null);
                } catch (Exception ignored) {}
            }
        }

        // 2. BULK INSERT
        repositorio.saveAll(nuevosHechos);

        // 3. un solo flush real
        em.flush();

        return nuevosHechos;
    }



  /*  public List<Hecho> sincronizar() {
        importarHechos("src/main/resources/prueba1.csv");
        List<Hecho> hechosASincronizar = repositorio.buscarNoSincronizados();
        hechosASincronizar.forEach(h -> h.setEstaSincronizado(true));
        return hechosASincronizar;
    }*/

    @Override
    @Transactional
    public List<Hecho> importarDesdeArchivo(MultipartFile archivo) {
        try {
            // Detectar charset (UTF-8 con BOM vs ISO-8859-1)
            Charset charset = detectCharset(archivo);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(archivo.getInputStream(), charset))) {

                String header = br.readLine();
                if (header == null) return List.of();

                // Normalizar encabezados
                String[] headers = header.replace("\uFEFF","")   // remover BOM
                        .split(";", -1);

                Map<String, Integer> idx = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    idx.put(headers[i].trim().toLowerCase(), i);
                }

                List<Hecho> hechos = new ArrayList<>();
                String line;

                while ((line = br.readLine()) != null) {
                    if (line.isBlank()) continue;

                    String[] col = line.split(";", -1);

                    Hecho h = new Hecho();

                    h.setTitulo(get(col, idx, "titulo"));
                    h.setDescripcion(get(col, idx, "descripcion"));
                    h.setCategoria(get(col, idx, "categoria"));
                    h.setProvincia(get(col, idx, "provincia"));

                    h.setLatitud(parseDouble(get(col, idx, "latitud")));
                    h.setLongitud(parseDouble(get(col, idx, "longitud")));

                    h.setFechaAcontecimiento(parseDate(
                            get(col, idx, "fecha del hecho"),
                            get(col, idx, "fechaacontecimiento")
                    ));

                    h.setFechaCarga(LocalDate.now());

                    // Id autogenerado
                    h.setIdHecho(null);

                    repositorio.save(h);
                    hechos.add(h);
                }

                em.flush();
                return hechos;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al procesar CSV: " + e.getMessage(), e);
        }
    }

    // HELPERS

    private Charset detectCharset(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        if (bytes.length >= 3 &&
                bytes[0] == (byte)0xEF &&
                bytes[1] == (byte)0xBB &&
                bytes[2] == (byte)0xBF) {
            return StandardCharsets.UTF_8;
        }
        return StandardCharsets.ISO_8859_1;
    }

    private String get(String[] col, Map<String,Integer> idx, String key) {
        Integer i = idx.get(key);
        if (i == null || i >= col.length) return null;
        String val = col[i].trim();
        if (val.isEmpty()) return null;
        return val;
    }

    private Double parseDouble(String s) {
        if (s == null) return null;
        return Double.valueOf(s.replace(",", "."));
    }

    private LocalDate parseDate(String... values) {
        for (String s : values) {
            if (s == null) continue;
            try { return LocalDate.parse(s, DateTimeFormatter.ofPattern("d/M/yyyy")); }
            catch (Exception ignored) {}
        }
        return null;
    }
}
