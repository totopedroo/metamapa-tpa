package ar.edu.utn.frba.server.servicioAgregador.controllers;
import ar.edu.utn.frba.server.fuente.estatica.services.FuenteEstaticaService;
import ar.edu.utn.frba.server.servicioAgregador.domain.*;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.services.*;
import ar.edu.utn.frba.server.servicioAgregador.dtos.ColeccionOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.algoritmos.navegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.server.servicioEstadisticas.services.ExportacionCSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.security.SecureRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@CrossOrigin("http://localhost:8080")
public class ColeccionController {
    private final ImportadorCSV importadorCSV;
    private final SecureRandom secureRandom = new SecureRandom();
    @Autowired
    private IColeccionService coleccionService;
    @Autowired
    private IrrestrictaStrategy irrestrictaStrategy;
    @Autowired
    private IHechosRepository hechosRepository;
    @Autowired
    private HechosService hechosService;
    @Autowired
    private SolicitudService solicitudService;
    @Autowired
    private FuenteEstaticaService fuenteEstaticaService;
    @Autowired
    private ExportacionCSVService exportacionCSVService;

    public ColeccionController(ImportadorCSV importadorCSV, IColeccionService coleccionService) {
        this.importadorCSV = importadorCSV;
        this.coleccionService = coleccionService;
    }

    @GetMapping("/colecciones")
    public ResponseEntity<List<ColeccionOutputDto>> listar() {
        var out = coleccionService.findAll().stream()
                .map(ColeccionOutputDto::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    @GetMapping("/colecciones/{id}/hechos")
    public List<HechosOutputDto> obtenerHechosDeColeccion(@PathVariable Long id) {
        return coleccionService.obtenerHechosPorColeccion(id).stream()
                .map(HechosOutputDto::fromModel)
                .toList();
    }

    @GetMapping("/colecciones/{id}/hechos/navegacion")
    public ResponseEntity<?> navegarHechos(
            @PathVariable Long id,
            @RequestParam(defaultValue = "irrestricta") String modo) {   // <-- String, no Strategy
        var hechos = coleccionService.navegarHechos(id, modo);
        var dtos = hechos.stream()
                .map(ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto::fromModel)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/colecciones/{coleccionId}/hechos/{hechoId}/consenso")
    public ResponseEntity<HechosOutputDto> obtenerConsenso(
            @PathVariable Long coleccionId,
            @PathVariable Long hechoId) {
        return ResponseEntity.ok(coleccionService.consensuarHecho(coleccionId, hechoId));
    }

    @GetMapping("/colecciones/{coleccionId}/hechos/filtrados")
    public ResponseEntity<List<HechosOutputDto>> filtrarHechosDeColeccion(
            @PathVariable Long coleccionId,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteHasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoHasta,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud
    ) {
        try {
            var hechos = coleccionService.filtrarHechosPorColeccion(
                    coleccionId, titulo, categoria,
                    fechaReporteDesde, fechaReporteHasta,
                    fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                    latitud, longitud
            );

            var dtos = hechos.stream()
                    .map(HechosOutputDto::fromModel)
                    .toList();

            return ResponseEntity.ok(dtos);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping("/colecciones/{coleccionId}/hechos/{hechoId}")
    public ResponseEntity<?> agregarHechoAColeccion(@PathVariable Long coleccionId, @PathVariable Long hechoId) {
        try {
            ColeccionOutputDto coleccionActualizada = coleccionService.agregarHechoAColeccion(coleccionId, hechoId);
            return ResponseEntity.ok(coleccionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/colecciones/{coleccionId}/hechos/{hechoId}/consensuar")
    public ResponseEntity<HechosOutputDto> consensuarHecho(
            @PathVariable Long coleccionId,
            @PathVariable Long hechoId) {
        try {
            HechosOutputDto dto = coleccionService.consensuarHecho(coleccionId, hechoId);
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
