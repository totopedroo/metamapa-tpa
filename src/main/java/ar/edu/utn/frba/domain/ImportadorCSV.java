package ar.edu.utn.frba.domain;

import ar.edu.utn.frba.Dtos.HechosInputDto;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ImportadorCSV implements Importador {

  private static final String CSV_DELIMITER = ";";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");
  private static final int EXPECTED_COLUMNS = 8;
  private final SecureRandom secureRandom = new SecureRandom();
  @Override
  public List<Hecho> importar(Fuente fuente) {
    List<Hecho> hechos = new ArrayList<>();
    String filePathString = fuente.getPath();

    if (filePathString == null || filePathString.trim().isEmpty()) {
      throw new IllegalArgumentException("La ruta del archivo CSV es nula o está vacía.");
    }

    Path filePath = Paths.get(filePathString);

    try (BufferedReader br = Files.newBufferedReader(filePath)) {
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

        if (values.length < EXPECTED_COLUMNS) {
          throw new RuntimeException("Fila con datos insuficientes en la línea " + lineNumber + ". Se esperaban " + EXPECTED_COLUMNS + " columnas, se encontraron " + values.length + ". Línea: " + trimmedLine);
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

          }
        }

        LocalDate fechaCarga = LocalDate.now();

        Hecho hecho = new Hecho(
                titulo,
                descripcion,
                categoria,
                contenidoMultimedia.orElse(null),
                latitud,
                longitud,
                fechaAcontecimiento,
                fechaCarga,
                idHecho
        );
        hechos.add(hecho);
      }
    } catch (IOException e) {
      throw new RuntimeException("No se pudo leer el archivo '" + filePathString + "'. Detalle: " + e.getMessage(), e);
    }
    System.out.println("ImportadorCSV: Se importaron " + hechos.size() + " hechos desde '" + filePathString + "'.");
    return hechos;
  }
}
