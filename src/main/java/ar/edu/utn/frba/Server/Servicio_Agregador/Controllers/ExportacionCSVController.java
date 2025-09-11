package ar.edu.utn.frba.Server.Servicio_Agregador.Controllers;

import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ExportacionCSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/exportar")
@CrossOrigin("http://localhost:8080")
public class ExportacionCSVController {

    @Autowired
    private ExportacionCSVService exportacionCSVService;

    /**
     * Exporta todos los hechos a CSV
     */
    @GetMapping("/hechos")
    public ResponseEntity<byte[]> exportarHechos() {
        try {
            byte[] csvData = exportacionCSVService.exportarHechos();
            return crearRespuestaCSV(csvData, "hechos_" + obtenerTimestamp() + ".csv");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exporta hechos por provincia a CSV
     */
    @GetMapping("/hechos/provincia/{provincia}")
    public ResponseEntity<byte[]> exportarHechosPorProvincia(@PathVariable String provincia) {
        try {
            byte[] csvData = exportacionCSVService.exportarHechosPorProvincia(provincia);
            return crearRespuestaCSV(csvData, "hechos_provincia_" + provincia + "_" + obtenerTimestamp() + ".csv");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exporta hechos por categoría a CSV
     */
    @GetMapping("/hechos/categoria/{categoria}")
    public ResponseEntity<byte[]> exportarHechosPorCategoria(@PathVariable String categoria) {
        try {
            byte[] csvData = exportacionCSVService.exportarHechosPorCategoria(categoria);
            return crearRespuestaCSV(csvData, "hechos_categoria_" + categoria + "_" + obtenerTimestamp() + ".csv");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exporta estadísticas generales a CSV
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<byte[]> exportarEstadisticas() {
        try {
            byte[] csvData = exportacionCSVService.exportarEstadisticas();
            return crearRespuestaCSV(csvData, "estadisticas_" + obtenerTimestamp() + ".csv");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exporta estadísticas por colección a CSV
     */
    @GetMapping("/estadisticas/coleccion/{coleccionId}")
    public ResponseEntity<byte[]> exportarEstadisticasPorColeccion(@PathVariable Long coleccionId) {
        try {
            byte[] csvData = exportacionCSVService.exportarEstadisticasPorColeccion(coleccionId);
            return crearRespuestaCSV(csvData,
                    "estadisticas_coleccion_" + coleccionId + "_" + obtenerTimestamp() + ".csv");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crea una respuesta HTTP con archivo CSV
     */
    private ResponseEntity<byte[]> crearRespuestaCSV(byte[] csvData, String nombreArchivo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", nombreArchivo);
        headers.setContentLength(csvData.length);

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    /**
     * Genera un timestamp para nombres de archivo
     */
    private String obtenerTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }
}
