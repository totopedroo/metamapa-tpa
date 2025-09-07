package ar.edu.utn.frba.Server.Servicio_Agregador.Controllers;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.*;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.*;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.ColeccionRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.HechosRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso.*;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.CuradaStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.ModoNavegacionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.*;
import java.security.SecureRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@CrossOrigin("http://localhost:8080")
public class ColeccionController {

    private final SecureRandom secureRandom = new SecureRandom();
    @Autowired
    private IColeccionService coleccionService;
    @Autowired
    private ISeederService seederService;

    @Autowired
    private IrrestrictaStrategy irrestrictaStrategy;
    @Autowired
    private final ar.edu.utn.frba.Server.Servicio_Agregador.Domain.ImportadorCSV importadorCSV;
    @Autowired
    private HechosRepository hechosRepository;
    @Autowired
    private HechosService hechosService;
    @Autowired
    private SolicitudService solicitudService;

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

    @GetMapping("/{id}/hechos")
    public List<Hecho> obtenerHechosDeColeccion(@PathVariable long id) {
        return coleccionService.obtenerHechosPorColeccion(id);
    }

    @GetMapping("/createColeccionAPI")
    public Coleccion crearColeccionPruebaAPI() {
        return coleccionService.setColeccionApi();
    }

    //TODO
    @PostMapping("/crearDesdeCSV")
    public ResponseEntity<ColeccionOutputDto> crearDesdeCSV(@RequestParam String path) {
        var creada = coleccionService.setColeccionCsv(path);
        return ResponseEntity.ok(ColeccionOutputDto.fromModel(creada));
    }
    //TODO
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

    @GetMapping("/colecciones/{id}/hechos/navegacion")
    public ResponseEntity<?> navegarHechos(
            @PathVariable Long id,
            @RequestParam(defaultValue = "irrestricta") ModoNavegacionStrategy modo) {
        try {
            // ahora el service resuelve la estrategia
            List<Hecho> hechos = coleccionService.navegarHechos(id, modo);

            // Devolver DTOs para evitar problemas de Optional/JSON
            var dtos = hechos.stream()
                    .map(ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto::fromModel)
                    .toList();

            return ResponseEntity.ok(dtos);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Colección no encontrada: " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage()); // "Modo de navegación inválido: ..."
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    @PutMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<Void> setearAlgoritmoPorNombre(
            @PathVariable Long id,
            @RequestParam String tipo) {

        try {
            AlgoritmoDeConsensoStrategy algoritmo = switch (tipo.toLowerCase()) {
                case "mayoriasimple" -> new MayoriaSimpleStrategy();
                case "multiplesmenciones" -> new MultiplesMencionesStrategy();
                case "absoluta" -> new AbsolutaStrategy();
                case "defecto" -> new ConsensoPorDefectoStrategy();
                default -> throw new IllegalArgumentException("Tipo de algoritmo desconocido: " + tipo);
            };

            coleccionService.setAlgoritmoDeConsenso(id, algoritmo);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/colecciones/{coleccionId}/hechos/{hechoId}/consensuar")
    public ResponseEntity<?> consensuarHecho(
            @PathVariable Long coleccionId,
            @PathVariable Long hechoId) {
        try {
            Hecho actualizado = coleccionService.consensuarHecho(coleccionId, hechoId);
            return ResponseEntity.ok("Hecho " + actualizado.getIdHecho() + " marcado como consensuado.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/colecciones/{coleccionId}/hechos/{hechoId}/agregar-fuente")
    public ResponseEntity<?> agregarFuenteAHecho(
            @PathVariable Long coleccionId,
            @PathVariable Long hechoId,
            @RequestParam Fuente fuente) {
        try {
            Hecho actualizado = coleccionService.agregarFuenteAHecho(coleccionId, hechoId, fuente);

            // devolver DTO para evitar problemas de serialización (Optional, etc.)
            var dto = ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto.fromModel(actualizado);
            return ResponseEntity.ok(dto);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/colecciones/{coleccionId}/hechos/{hechoId}/quitar-fuente")
    public ResponseEntity<?> quitarFuenteDeHecho(
            @PathVariable Long coleccionId,
            @PathVariable Long hechoId) {
        try {
            Hecho actualizado = coleccionService.quitarFuenteDeHecho(coleccionId, hechoId);

            // Devolver DTO para no chocar con Optional/JSON
            var dto = ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto.fromModel(actualizado);
            return ResponseEntity.ok(dto);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/colecciones/{coleccionId}/hechos/filtrados")
    public ResponseEntity<?> filtrarHechosPorColeccion(
            @PathVariable Long coleccionId,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String categoria) {
        try {
            List<Hecho> hechos = coleccionService.filtrarHechosPorColeccion(coleccionId, titulo, categoria);

            // Evita problemas de Optional/serialización devolviendo DTOs
            var dtos = hechos.stream()
                    .map(ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto::fromModel)
                    .toList();

            return ResponseEntity.ok(dtos);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/solicitudes/{id}/aprobar")
    public ResponseEntity<Void> aprobarSolicitud(@PathVariable Long id) {
        try {
            solicitudService.aceptarSolicitud(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/solicitudes/{id}/denegar")
    public ResponseEntity<Void> denegarSolicitud(@PathVariable Long id) {
        try {
            solicitudService.rechazarSolicitud(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/debug/csv")
    public ResponseEntity<?> debugCsv(@RequestParam String archivo) {
        try {
            boolean fs = java.nio.file.Files.exists(java.nio.file.Path.of(archivo));
            boolean res = new org.springframework.core.io.ClassPathResource(archivo).exists();
            return ResponseEntity.ok("CSV encontrado? filesystem=" + fs + " | resources=" + res + " | valor='" + archivo + "'");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("debug error: " + e.getMessage());
        }
    }

    @PostMapping(value = "/crearDesdeCSVHardcoded", produces = "application/json")
    public ResponseEntity<ColeccionOutputDto> crearDesdeCSVHardcoded() {
        Coleccion c = coleccionService.crearColeccionDesdeCSVHardcoded("Colección CSV (hardcoded)");
        return ResponseEntity.ok(ColeccionOutputDto.fromModel(c));
    }
}

