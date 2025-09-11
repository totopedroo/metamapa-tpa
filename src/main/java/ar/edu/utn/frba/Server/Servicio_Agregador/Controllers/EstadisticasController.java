package ar.edu.utn.frba.Server.Servicio_Agregador.Controllers;

import ar.edu.utn.frba.Server.Servicio_Agregador.Service.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/estadisticas")
@CrossOrigin("http://localhost:8080")
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    /**
     * Obtiene la provincia con mayor cantidad de hechos reportados
     */
    @GetMapping("/provincia-mas-hechos")
    public ResponseEntity<Map<String, Object>> obtenerProvinciaConMasHechos() {
        Map<String, Object> resultado = estadisticasService.obtenerProvinciaConMasHechos();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Obtiene la categoría con mayor cantidad de hechos reportados
     */
    @GetMapping("/categoria-mas-hechos")
    public ResponseEntity<Map<String, Object>> obtenerCategoriaConMasHechos() {
        Map<String, Object> resultado = estadisticasService.obtenerCategoriaConMasHechos();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Obtiene la provincia con mayor cantidad de hechos de una categoría específica
     */
    @GetMapping("/provincia-mas-hechos-categoria/{categoria}")
    public ResponseEntity<Map<String, Object>> obtenerProvinciaConMasHechosDeCategoria(
            @PathVariable String categoria) {
        Map<String, Object> resultado = estadisticasService.obtenerProvinciaConMasHechosDeCategoria(categoria);
        return ResponseEntity.ok(resultado);
    }

    /**
     * Obtiene la hora del día con mayor cantidad de hechos de una categoría
     * específica
     */
    @GetMapping("/hora-mas-hechos-categoria/{categoria}")
    public ResponseEntity<Map<String, Object>> obtenerHoraConMasHechosDeCategoria(
            @PathVariable String categoria) {
        Map<String, Object> resultado = estadisticasService.obtenerHoraConMasHechosDeCategoria(categoria);
        return ResponseEntity.ok(resultado);
    }

    /**
     * Cuenta cuántas solicitudes de eliminación son spam
     */
    @GetMapping("/solicitudes-spam")
    public ResponseEntity<Map<String, Object>> contarSolicitudesSpam() {
        Map<String, Object> resultado = estadisticasService.contarSolicitudesSpam();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Obtiene estadísticas generales del sistema
     */
    @GetMapping("/generales")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasGenerales() {
        Map<String, Object> resultado = estadisticasService.obtenerEstadisticasGenerales();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Obtiene estadísticas por colección específica
     */
    @GetMapping("/coleccion/{coleccionId}")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasPorColeccion(
            @PathVariable Long coleccionId) {
        Map<String, Object> resultado = estadisticasService.obtenerEstadisticasPorColeccion(coleccionId);
        return ResponseEntity.ok(resultado);
    }
}
