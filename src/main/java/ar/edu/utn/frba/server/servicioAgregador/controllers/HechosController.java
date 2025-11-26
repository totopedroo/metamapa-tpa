package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechoDTO;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.services.IHechosService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hechos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8082")
public class HechosController {

    private final IHechosService hechosService;

    // ENDPOINT PARA LANDING (Resuelve el error 500)
    // GET /api/hechos?modo=irrestricto&limit=5
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerHechos(
        @RequestParam(required = false) String modo,
        @RequestParam(required = false) Integer limit,
        // Parámetros para el filtro avanzado (se pueden combinar o separar)
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteHasta,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoDesde,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoHasta,
        @RequestParam(required = false) Double latitud,
        @RequestParam(required = false) Double longitud) {

        // Si viene 'modo', asumimos que es la Landing Page pidiendo los últimos N
        if (modo != null) {
            int limite = (limit != null) ? limit : 5;
            List<HechoDTO> hechos = hechosService.obtenerHechosLanding(modo, limite);
            return ResponseEntity.ok(Map.of("items", hechos));
        }

        // Si no viene 'modo', asumimos que es la búsqueda avanzada
        List<HechosOutputDto> filtrados = hechosService.filtrarHechos(categoria, fechaReporteDesde, fechaReporteHasta,
            fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);

        // Para búsqueda avanzada, podemos devolver la lista directa o wrappeada.
        // Por compatibilidad, devolvemos items.
        return ResponseEntity.ok(Map.of("items", filtrados));
    }

    @PostMapping(path = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HechosOutputDto crearHecho(@RequestBody HechosInputDto inputDto) {
        return hechosService.crearHecho(inputDto);
    }

    @PostMapping("/importar-api")
    public ResponseEntity<?> importarApi() {
        List<Hecho> guardados = hechosService.importarDesdeApi();
        return ResponseEntity.ok(Map.of("insertados", guardados.size()));
    }

    @GetMapping("/hechos/usuario/{idUsuario}")
    public List<HechosOutputDto> listarHechosPorUsuario(@PathVariable Long idUsuario) {
        return hechosService.listarHechosPorUsuario(idUsuario);
    }
}