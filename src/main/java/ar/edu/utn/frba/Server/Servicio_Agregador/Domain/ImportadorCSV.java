package ar.edu.utn.frba.Server.Servicio_Agregador.Domain;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Component("importadorCSVAgregador")
public class ImportadorCSV {

  private static final String CSV_DELIMITER = ";";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");
  private static final int EXPECTED_COLUMNS = 7;
  private final SecureRandom secureRandom = new SecureRandom();

  public List<Hecho> importar(String filePathString) {
    List<Hecho> hechos = new ArrayList<>();

    if (filePathString == null || filePathString.trim().isEmpty()) {
      throw new IllegalArgumentException("La ruta del archivo CSV es nula o está vacía.");
    }

    Path filePath = Paths.get(filePathString);

    try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.ISO_8859_1)) {
      String line;
      boolean isHeader = true;
      int lineNumber = 0;

      while ((line = br.readLine()) != null) {
        lineNumber++;

        if (isHeader) {
          isHeader = false;
          continue;
        }

        String trimmedLine = line.trim();

        if (trimmedLine.isEmpty()) {
          continue;
        }
        String[] values = trimmedLine.split(CSV_DELIMITER);

        validarLinea(values, lineNumber);

        if (values.length < EXPECTED_COLUMNS) {
          throw new RuntimeException("Fila con datos insuficientes en la línea " + lineNumber + ". Se esperaban "
              + EXPECTED_COLUMNS + " columnas, se encontraron " + values.length + ". Línea: " + trimmedLine);
        }

        long idHecho;

        do {
          idHecho = secureRandom.nextLong();
        } while (idHecho <= 0);

        String titulo = values[0].trim();
        String descripcion = values[1].trim();
        String categoria = values[2].trim();
        Optional<ContenidoMultimedia> contenidoMultimedia = Optional.empty();
        String contenidoMultimediaStr = values[3].trim();
        if (!"null".equalsIgnoreCase(contenidoMultimediaStr) && !contenidoMultimediaStr.isEmpty()) {
        }
        Double latitud = null;
        String latitudStr = values[4].trim();
        if (!latitudStr.isEmpty()) {
          latitud = Double.parseDouble(latitudStr.replace(',', '.'));
        }

        Double longitud = null;
        String longitudStr = values[5].trim();
        if (!longitudStr.isEmpty()) {
          longitud = Double.parseDouble(longitudStr.replace(',', '.'));
        }

        LocalDate fechaAcontecimiento = null;
        String fechaAcontecimientoStr = values[6].trim();
        if (!fechaAcontecimientoStr.isEmpty()) {
          fechaAcontecimiento = LocalDate.parse(fechaAcontecimientoStr, DATE_FORMATTER);
        }

        List<String> etiquetas = new ArrayList<>();
        if (values.length > 7) {
          String etiquetasStr = values[7].trim();
          if (!"null".equalsIgnoreCase(etiquetasStr) && !etiquetasStr.isEmpty()) {
            String[] etiquetasArray = etiquetasStr.split(",");
            for (String etiqueta : etiquetasArray) {
              etiquetas.add(etiqueta.trim());
            }
          }
        }

        LocalDate fechaCarga = LocalDate.now();

        TipoFuente fuente = TipoFuente.LOCAL;
        Hecho hecho = new Hecho(
            titulo,
            descripcion,
            categoria,
            contenidoMultimedia.orElse(null),
            latitud,
            longitud,
            fechaAcontecimiento,
            fechaCarga,
            idHecho,
                fuente);
        hechos.add(hecho);
      }
    } catch (IOException e) {
      throw new RuntimeException("No se pudo leer el archivo '" + filePathString + "'. Detalle: " + e.getMessage(), e);
    }
    System.out.println("ImportadorCSV: Se importaron " + hechos.size() + " hechos desde '" + filePathString + "'.");
    return hechos;
  }

  private void validarFecha(String fechaStr, int lineNumber) {
    try {
      if (fechaStr == null || fechaStr.trim().isEmpty()) {
        System.out.println("Warning - Línea " + lineNumber + ": Fecha vacía");
        return;
      }

      LocalDate fecha = LocalDate.parse(fechaStr, DATE_FORMATTER);

      if (fecha.isAfter(LocalDate.now())) {
        System.out.println("Warning - Línea " + lineNumber + ": Fecha futura detectada '" + fechaStr + "'");
      }

      if (fecha.isBefore(LocalDate.of(1900, 1, 1))) {
        System.out.println("Warning - Línea " + lineNumber + ": Fecha anterior a 1900 detectada '" + fechaStr + "'");
      }
    } catch (Exception e) {
      System.out.println("Warning - Línea " + lineNumber + ": Error en formato de fecha '" + fechaStr + "'");
    }
  }

  private void validarCoordenadas(String latitudStr, String longitudStr, int lineNumber) {
    try {
      if (!latitudStr.isEmpty()) {
        double latitud = Double.parseDouble(latitudStr.replace(',', '.'));
        if (latitud < -90 || latitud > 90) {
          System.out.println("Warning - Línea " + lineNumber + ": Latitud fuera de rango '" + latitudStr + "'");
        }
      }

      if (!longitudStr.isEmpty()) {
        double longitud = Double.parseDouble(longitudStr.replace(',', '.'));
        if (longitud < -180 || longitud > 180) {
          System.out.println("Warning - Línea " + lineNumber + ": Longitud fuera de rango '" + longitudStr + "'");
        }
      }
    } catch (Exception e) {
      System.out.println("Warning - Línea " + lineNumber + ": Error en formato de coordenadas");
    }
  }

  private void validarCamposObligatorios(String[] values, int lineNumber) {
    try {
      if (values[0].trim().isEmpty()) {
        System.out.println("Warning - Línea " + lineNumber + ": Título vacío");
      }
      if (values[1].trim().isEmpty()) {
        System.out.println("Warning - Línea " + lineNumber + ": Descripción vacía");
      }
      if (values[2].trim().isEmpty()) {
        System.out.println("Warning - Línea " + lineNumber + ": Categoría vacía");
      }
    } catch (Exception e) {
      System.out.println("Warning - Línea " + lineNumber + ": Error validando campos obligatorios");
    }
  }

  private void validarLinea(String[] values, int lineNumber) {
    try {
      validarCamposObligatorios(values, lineNumber);
      validarCoordenadas(values[4], values[5], lineNumber);
      validarFecha(values[6], lineNumber);
    } catch (Exception e) {
      System.out.println("Warning - Error en validación de línea " + lineNumber + ": " + e.getMessage());
    }
  }

  public Hecho parsearLineaCSV(String line) {
    if (line == null || line.trim().isEmpty()) {
      return null;
    }

    String[] values = line.trim().split(CSV_DELIMITER);

    if (values.length < EXPECTED_COLUMNS) {
      throw new RuntimeException("Línea con datos insuficientes. Se esperaban " + EXPECTED_COLUMNS
          + " columnas, se encontraron " + values.length);
    }

    long idHecho;
    do {
      idHecho = secureRandom.nextLong();
    } while (idHecho <= 0);

    String titulo = values[0].trim();
    String descripcion = values[1].trim();
    String categoria = values[2].trim();
    Optional<ContenidoMultimedia> contenidoMultimedia = Optional.empty();

    Double latitud = null;
    String latitudStr = values[4].trim();
    if (!latitudStr.isEmpty()) {
      latitud = Double.parseDouble(latitudStr.replace(',', '.'));
    }

    Double longitud = null;
    String longitudStr = values[5].trim();
    if (!longitudStr.isEmpty()) {
      longitud = Double.parseDouble(longitudStr.replace(',', '.'));
    }

    LocalDate fechaAcontecimiento = null;
    String fechaAcontecimientoStr = values[6].trim();
    if (!fechaAcontecimientoStr.isEmpty()) {
      fechaAcontecimiento = LocalDate.parse(fechaAcontecimientoStr, DATE_FORMATTER);
    }

    LocalDate fechaCarga = LocalDate.now();

    TipoFuente fuente = TipoFuente.LOCAL;
    return new Hecho(
        titulo,
        descripcion,
        categoria,
        contenidoMultimedia.orElse(null),
        latitud,
        longitud,
        fechaAcontecimiento,
        fechaCarga,
        idHecho,
            fuente);
  }
}
