package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechoFrontDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosInputDto;
import ar.edu.utn.frba.server.servicioAgregador.mappers.HechoFrontMapper;
import ar.edu.utn.frba.server.servicioAgregador.services.IHechosService;
import ar.edu.utn.frba.server.fuente.estatica.services.IFuenteEstaticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hechos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8082")
public class HechosController {

    private final IHechosService hechosService;
    private final IFuenteEstaticaService estaticaService;

    /**
     * GET /api/hechos
     * - Si viene 'modo' → landing page (usa HechoDTO simplificado)
     * - Si no viene → búsqueda avanzada (devuelve HechoFrontDto)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerHechos(
            @RequestParam(required = false) String modo,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteHasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoHasta,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud) {

        // === LANDING PAGE ===
        if (modo != null) {
            int limite = (limit != null) ? limit : 5;
            var hechosLanding = hechosService.obtenerHechosLanding(modo, limite);
            // Este endpoint sigue usando HechoDTO simplificado
            return ResponseEntity.ok(Map.of("items", hechosLanding));
        }

        // === BÚSQUEDA AVANZADA ===
        var hechos = hechosService.filtrarHechos(
                categoria, fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                latitud, longitud
        );

        // Convertimos SIEMPRE al DTO del FRONT
        var salida = hechos.stream()
                .map(HechoFrontMapper::toDto)
                .toList();

        return ResponseEntity.ok(Map.of("items", salida));
    }

    /**
     * POST /api/hechos/crear
     * Devuelve siempre el DTO del FRONT
     */
    @PostMapping(
            path = "/crear",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public HechoFrontDto crearHecho(@RequestBody HechosInputDto inputDto) {
        var creado = hechosService.crearHecho(inputDto);
        return HechoFrontMapper.toDto(creado);
    }

    /**
     * POST /api/hechos/importar-api
     */
    @PostMapping("/importar-api")
    public ResponseEntity<?> importarApi() {
        List<Hecho> guardados = hechosService.importarDesdeApi();
        return ResponseEntity.ok(Map.of("insertados", guardados.size()));
    }

    /**
     * GET /api/hechos/hechos/usuario/{idUsuario}
     * También homogenizado al front DTO
     */
    @GetMapping("/hechos/usuario/{idUsuario}")
    public List<HechoFrontDto> listarHechosPorUsuario(@PathVariable Long idUsuario) {
        return hechosService.listarHechosPorUsuario(idUsuario).stream()
                .map(HechoFrontMapper::toDto)
                .toList();
    }

    @PostMapping(
            path = "/importar-csv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> importarCsv(@RequestParam("file") MultipartFile file) {
        try {
            List<Hecho> hechos = estaticaService.importarDesdeArchivo(file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "importados", hechos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", e.getMessage()
                    ));
        }
    }

}