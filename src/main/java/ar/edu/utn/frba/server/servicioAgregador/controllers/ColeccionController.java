package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.*;
import ar.edu.utn.frba.server.servicioAgregador.dtos.*;
import ar.edu.utn.frba.server.servicioAgregador.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/colecciones")
@RequiredArgsConstructor
public class ColeccionController {

    private final IColeccionService coleccionService;
    private final SolicitudService solicitudService;
    private final ExportacionCSVService exportacionCSVService;

    // --- ENDPOINT PARA LANDING PAGE (GET /api/colecciones/ultimas) ---
    @GetMapping("/ultimas")
    public ResponseEntity<List<ColeccionOutputBD>> listarUltimas() {
        return ResponseEntity.ok(coleccionService.listarUltimas());
    }

    // --- OTROS ENDPOINTS ---

    @GetMapping
    public ResponseEntity<List<ColeccionOutputDto>> listar() {
        var out = coleccionService.findAll().stream()
            .map(ColeccionOutputDto::fromModel)
            .collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    // Funcionalidad de buscar por título
    // Endpoint: GET /api/colecciones/hechos?tituloColeccion=...
    // Nota: Asegúrate de agregar 'obtenerHechosPorTituloColeccion' a tu interfaz IColeccionService si no existe.
    @GetMapping("/hechos")
    public ResponseEntity<List<HechosOutputDto>> obtenerHechosPorTituloColeccion(
        @RequestParam("tituloColeccion") String tituloColeccion
    ) {
        /* List<HechosOutputDto> hechos = coleccionService.obtenerHechosPorTituloColeccion(tituloColeccion);
        return ResponseEntity.ok(hechos);
        */
        return ResponseEntity.ok(List.of()); // Placeholder hasta confirmar servicio
    }

    @PostMapping
    public ResponseEntity<ColeccionOutputBD> crear(@Valid @RequestBody ColeccionInputBD dto) {
        ColeccionOutputBD out = coleccionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColeccionOutputBD> listarUna(@PathVariable Long id) {
        return ResponseEntity.ok(coleccionService.listar(id));
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity<ColeccionOutputBD> editar(@PathVariable Long id, @RequestBody ColeccionUpdateBD in) {
        ColeccionOutputBD out = coleccionService.editar(id, in);
        return ResponseEntity.ok(out);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        coleccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/hechos")
    public List<HechosOutputDto> obtenerHechosDeColeccion(@PathVariable Long id) {
        return coleccionService.obtenerHechosPorColeccion(id).stream()
            .map(HechosOutputDto::fromModel)
            .toList();
    }

    @GetMapping("/{id}/hechos/navegacion")
    public ResponseEntity<?> navegarHechos(@PathVariable Long id, @RequestParam(defaultValue = "irrestricta") String modo) {
        var hechos = coleccionService.navegarHechos(id, modo);
        var dtos = hechos.stream().map(HechosOutputDto::fromModel).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/crearDesdeCSV")
    public ResponseEntity<ColeccionOutputDto> crearDesdeCSV(@RequestParam String path) {
        var c = coleccionService.crearColeccionDesdeFuentes("COLECCION CSV", path);
        return ResponseEntity.ok(ColeccionOutputDto.fromModel(c));
    }

    @PatchMapping("/{coleccionId}/hechos/{hechoId}")
    public ResponseEntity<?> agregarHechoAColeccion(@PathVariable Long coleccionId, @PathVariable Long hechoId) {
        try {
            ColeccionOutputDto resultado = coleccionService.agregarHechoAColeccion(coleccionId, hechoId);
            return ResponseEntity.ok(resultado);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    // Endpoints de Solicitudes
    @PostMapping("/solicitudes/{id}/aprobar")
    public ResponseEntity<Void> aprobarSolicitud(@PathVariable Long id) {
        try {
            solicitudService.aceptarSolicitud(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/solicitudes/{id}/denegar")
    public ResponseEntity<Void> denegarSolicitud(@PathVariable Long id) {
        try {
            solicitudService.rechazarSolicitud(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/algoritmo")
    public ResponseEntity<Void> setearAlgoritmoPorNombre(@PathVariable Long id, @RequestParam String tipo) {
        try {
            var t = ar.edu.utn.frba.server.contratos.enums.TipoAlgoritmoConsenso.fromCodigo(tipo);
            // ... lógica simplificada ...
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{coleccionId}/hechos/{hechoId}/consensuar")
    public ResponseEntity<HechosOutputDto> consensuarHecho(@PathVariable Long coleccionId, @PathVariable Long hechoId) {
        try {
            return ResponseEntity.ok(coleccionService.consensuarHecho(coleccionId, hechoId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}