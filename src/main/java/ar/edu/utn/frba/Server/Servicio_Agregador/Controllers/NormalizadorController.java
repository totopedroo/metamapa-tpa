package ar.edu.utn.frba.Server.Servicio_Agregador.Controllers;

import ar.edu.utn.frba.Server.Servicio_Agregador.Service.NormalizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/normalizar")
@CrossOrigin("http://localhost:8080")
public class NormalizadorController {

    @Autowired
    private NormalizadorService normalizadorService;

    /**
     * Normaliza una categoría
     */
    @PostMapping("/categoria")
    public ResponseEntity<Map<String, String>> normalizarCategoria(@RequestBody Map<String, String> request) {
        String categoria = request.get("categoria");
        String categoriaNormalizada = normalizadorService.normalizarCategoria(categoria);

        Map<String, String> response = new HashMap<>();
        response.put("original", categoria);
        response.put("normalizada", categoriaNormalizada);

        return ResponseEntity.ok(response);
    }

    /**
     * Normaliza una provincia
     */
    @PostMapping("/provincia")
    public ResponseEntity<Map<String, String>> normalizarProvincia(@RequestBody Map<String, String> request) {
        String provincia = request.get("provincia");
        String provinciaNormalizada = normalizadorService.normalizarProvincia(provincia);

        Map<String, String> response = new HashMap<>();
        response.put("original", provincia);
        response.put("normalizada", provinciaNormalizada);

        return ResponseEntity.ok(response);
    }

    /**
     * Normaliza una fecha
     */
    @PostMapping("/fecha")
    public ResponseEntity<Map<String, Object>> normalizarFecha(@RequestBody Map<String, String> request) {
        String fechaStr = request.get("fecha");
        LocalDate fechaNormalizada = normalizadorService.normalizarFecha(fechaStr);

        Map<String, Object> response = new HashMap<>();
        response.put("original", fechaStr);
        response.put("normalizada", fechaNormalizada != null ? fechaNormalizada.toString() : null);
        response.put("valida", fechaNormalizada != null);

        return ResponseEntity.ok(response);
    }

    /**
     * Normaliza un título
     */
    @PostMapping("/titulo")
    public ResponseEntity<Map<String, String>> normalizarTitulo(@RequestBody Map<String, String> request) {
        String titulo = request.get("titulo");
        String tituloNormalizado = normalizadorService.normalizarTitulo(titulo);

        Map<String, String> response = new HashMap<>();
        response.put("original", titulo);
        response.put("normalizada", tituloNormalizado);

        return ResponseEntity.ok(response);
    }

    /**
     * Normaliza una latitud
     */
    @PostMapping("/latitud")
    public ResponseEntity<Map<String, Object>> normalizarLatitud(@RequestBody Map<String, Double> request) {
        Double latitud = request.get("latitud");
        Double latitudNormalizada = normalizadorService.normalizarLatitud(latitud);

        Map<String, Object> response = new HashMap<>();
        response.put("original", latitud);
        response.put("normalizada", latitudNormalizada);
        response.put("valida", latitudNormalizada != null);

        return ResponseEntity.ok(response);
    }

    /**
     * Normaliza una longitud
     */
    @PostMapping("/longitud")
    public ResponseEntity<Map<String, Object>> normalizarLongitud(@RequestBody Map<String, Double> request) {
        Double longitud = request.get("longitud");
        Double longitudNormalizada = normalizadorService.normalizarLongitud(longitud);

        Map<String, Object> response = new HashMap<>();
        response.put("original", longitud);
        response.put("normalizada", longitudNormalizada);
        response.put("valida", longitudNormalizada != null);

        return ResponseEntity.ok(response);
    }

    /**
     * Normaliza coordenadas
     */
    @PostMapping("/coordenadas")
    public ResponseEntity<Map<String, Object>> normalizarCoordenadas(@RequestBody Map<String, Double> request) {
        Double latitud = request.get("latitud");
        Double longitud = request.get("longitud");

        Double latitudNormalizada = normalizadorService.normalizarLatitud(latitud);
        Double longitudNormalizada = normalizadorService.normalizarLongitud(longitud);

        Map<String, Object> response = new HashMap<>();
        response.put("latitud_original", latitud);
        response.put("latitud_normalizada", latitudNormalizada);
        response.put("longitud_original", longitud);
        response.put("longitud_normalizada", longitudNormalizada);
        response.put("latitud_valida", latitudNormalizada != null);
        response.put("longitud_valida", longitudNormalizada != null);

        return ResponseEntity.ok(response);
    }
}
