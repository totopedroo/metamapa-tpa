package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechoFrontDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosInputDto;
import ar.edu.utn.frba.server.servicioAgregador.mappers.HechoFrontMapper;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.services.IHechosService;
import ar.edu.utn.frba.server.fuente.estatica.services.IFuenteEstaticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hechos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
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

        // PAGINACIÓN
        Pageable pageable = PageRequest.of(page, size);

        Page<Hecho> paged = hechosService.filtrarHechosPaginado(
                categoria, fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                latitud, longitud, pageable
        );

        var salida = paged.getContent().stream()
                .map(HechoFrontMapper::toDto)
                .toList();

        return ResponseEntity.ok(Map.of(
                "items", salida,
                "totalItems", paged.getTotalElements(),
                "totalPages", paged.getTotalPages(),
                "page", page,
                "size", size
        ));
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
        return hechosService.listarHechosPorUsuario2(idUsuario).stream()
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

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarHecho(@PathVariable Long id) {

        // 1. Obtener rol desde el JWT parseado
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getAuthorities().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Debe autenticarse para eliminar hechos"));
        }

        String rol = auth.getAuthorities().iterator().next().getAuthority();  // ej: "ROLE_ADMIN"

        // 2. Validar rol
        if (!"ROLE_ADMIN".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Solo un ADMIN puede eliminar hechos"));
        }

        // 3. Ejecutar operación en el servicio
        boolean eliminado = hechosService.eliminarHecho(id);

        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Hecho no encontrado"));
        }

        // 4. Éxito
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/titulos")
    public ResponseEntity<Map<Long, String>> obtenerTitulosPorIds(
            @RequestParam List<Long> ids
    ) {
        // No pedimos todos los hechos. Solo buscamos los necesarios.
        Map<Long, String> titulos = ids.stream()
                .map(id -> hechosService.buscarPorId(id))  // usando findById en service
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(
                        h -> h.getIdHecho(),
                        h -> h.getTitulo()
                ));

        return ResponseEntity.ok(titulos);
    }
}