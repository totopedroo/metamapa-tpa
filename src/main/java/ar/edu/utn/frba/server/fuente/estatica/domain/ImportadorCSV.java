package ar.edu.utn.frba.server.fuente.estatica.domain;

import ar.edu.utn.frba.server.servicioAgregador.domain.ContenidoMultimedia;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component("importadorCSVFuenteEstatica")
public class ImportadorCSV {

  private static final Logger log = LoggerFactory.getLogger(ImportadorCSV.class);

  private static final DateTimeFormatter[] FORMATS = new DateTimeFormatter[]{
          DateTimeFormatter.ofPattern("d/M/yyyy"),
          DateTimeFormatter.ISO_LOCAL_DATE
  };

  /**
   * Importación rápida usando OpenCSV (streaming, sin cargar todo el archivo).
   */
  public List<Hecho> importar(String path) {
    List<Hecho> hechos = new ArrayList<>();

    try (CSVReader reader = new CSVReader(new FileReader(path))) {

      String[] row;
      int line = 0;

      // Leer encabezado (lo salteamos)
      reader.readNext();

      while ((row = reader.readNext()) != null) {
        line++;

        try {
          Hecho h = parseRow(row);
          if (h != null) {
            hechos.add(h);
          }
        } catch (Exception e) {
          log.warn("[CSV] Error en fila {}: {}", line, e.getMessage());
        }
      }

    } catch (Exception e) {
      throw new RuntimeException("Error leyendo CSV: " + e.getMessage(), e);
    }

    return hechos;
  }

  /**
   * Mapea una fila completa del CSV a un Hecho.
   */
  private Hecho parseRow(String[] v) {
    if (v.length < 7) {
      return null; // fila inválida
    }

    String titulo = trim(v[0]);
    String descripcion = trim(v[1]);
    String categoria = trim(v[2]);
    String url = trim(v[3]);

    Double latitud = parseDouble(trim(v[4]));
    Double longitud = parseDouble(trim(v[5]));

    LocalDate fechaAcontecimiento = parseDate(trim(v[6]));
    LocalTime horaAcontecimiento = tryParseTime(trimSafe(v, 7));

    LocalDate fechaCarga = LocalDate.now();

    // Multimedia opcional
    ContenidoMultimedia cm = null;
    if (url != null && !url.isBlank()) {
      cm = new ContenidoMultimedia();
      cm.setUrl(url);
    }

    Hecho h = Hecho.builder()
            .titulo(titulo)
            .descripcion(descripcion)
            .categoria(categoria)
            .contenidoMultimedia(cm)
            .latitud(latitud)
            .longitud(longitud)
            .fechaAcontecimiento(fechaAcontecimiento)
            .horaAcontecimiento(horaAcontecimiento)
            .fechaCarga(fechaCarga)
            .build();

    // NO seteamos idHecho → lo asigna servicio
    return h;
  }

  // ------------------------------------------------------------
  // HELPER METHODS
  // ------------------------------------------------------------

  private String trim(String s) {
    return s == null ? null : s.trim();
  }

  private String trimSafe(String[] arr, int index) {
    return (index < arr.length) ? trim(arr[index]) : null;
  }

  private Double parseDouble(String s) {
    try {
      if (s == null || s.isBlank()) return null;
      return Double.valueOf(s.replace(",", "."));
    } catch (Exception e) {
      return null;
    }
  }

  private LocalDate parseDate(String s) {
    if (s == null || s.isBlank()) return null;

    for (DateTimeFormatter f : FORMATS) {
      try {
        return LocalDate.parse(s, f);
      } catch (Exception ignored) { }
    }

    log.warn("[CSV] Fecha inválida '{}'", s);
    return null;
  }

  private LocalTime tryParseTime(String s) {
    if (s == null || s.isBlank()) return null;
    try {
      return LocalTime.parse(s);
    } catch (Exception e) {
      return null;
    }
  }
}