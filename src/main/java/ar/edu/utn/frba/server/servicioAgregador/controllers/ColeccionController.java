package ar.edu.utn.frba.server.servicioAgregador.controllers;
import ar.edu.utn.frba.server.fuente.estatica.services.FuenteEstaticaService;
import ar.edu.utn.frba.server.servicioAgregador.domain.*;
import ar.edu.utn.frba.server.servicioAgregador.dtos.*;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.services.*;
import ar.edu.utn.frba.server.servicioAgregador.domain.navegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.security.SecureRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/colecciones")
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



   @GetMapping("")
    public ResponseEntity<List<ColeccionOutputDto>> listar() {
        var out = coleccionService.findAll().stream()
                .map(ColeccionOutputDto::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(out);
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
    public ResponseEntity<ColeccionOutputBD> editar(
            @PathVariable Long id,
            @RequestBody ColeccionUpdateBD in
    ) {
        ColeccionOutputBD out = coleccionService.editar(id, in);
        return ResponseEntity.ok(out);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        coleccionService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204
    }

@PostMapping("/importar-csv-ruta")
    public ResponseEntity<String> importarDesdeRutaCSV(@RequestParam("ruta") String rutaArchivo) {
        try {

            List<Hecho> hechosImportados = exportacionCSVService.importarDesdeRuta(rutaArchivo);
            return ResponseEntity.ok("Se importaron " + hechosImportados.size() + " hechos desde " + rutaArchivo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al importar desde ruta: " + e.getMessage());
        }
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
    public ResponseEntity<?> filtrarHechosPorColeccion(
            @PathVariable Long coleccionId,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String categoria) {
        try {
            List<Hecho> hechos = coleccionService.filtrarHechosPorColeccion(coleccionId, titulo, categoria);

            // Evita problemas de Optional/serialización devolviendo DTOs
            var dtos = hechos.stream()
                    .map(ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto::fromModel)
                    .toList();

            return ResponseEntity.ok(dtos);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/debug/csv")
    public ResponseEntity<?> debugCsv(@RequestParam String archivo) {
        try {
            boolean fs = false;
            boolean res = false;
            String valor = archivo;

            if (archivo != null && archivo.startsWith("classpath:")) {
                String cp = archivo.substring("classpath:".length()); // "archivodefinitivo.csv"
                res = new org.springframework.core.io.ClassPathResource(cp).exists();
            } else if (archivo != null && !archivo.isBlank()) {
                java.nio.file.Path p = java.nio.file.Path.of(archivo);
                fs = java.nio.file.Files.exists(p);
            }

            return ResponseEntity.ok(
                    "CSV encontrado? filesystem=" + fs + " | resources=" + res + " | valor='" + valor + "'"
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("debug error: " + e.getMessage());
        }
    }

    @PostMapping("/colecciones/desdeFuentes")
    public ResponseEntity<ColeccionOutputDto> crearDesdeFuentes(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String criterio) {
        var c = coleccionService.crearColeccionDesdeFuentes(titulo, criterio);
        return ResponseEntity.ok(ColeccionOutputDto.fromModel(c));
    }

    @PostMapping("/crearDesdeCSV")
    public ResponseEntity<ColeccionOutputDto> crearDesdeCSV(@RequestParam String path) {
        var c = coleccionService.crearColeccionDesdeFuentes("COLECCION CSV", path);
        return ResponseEntity.ok(ColeccionOutputDto.fromModel(c));
    }

    @PatchMapping("/{coleccionId}/hechos/{hechoId}")
    public ResponseEntity<?> agregarHechoAColeccion(
            @PathVariable Long coleccionId,
            @PathVariable Long hechoId) {

        try {
            ColeccionOutputDto resultado = coleccionService.agregarHechoAColeccion(coleccionId, hechoId);

            // HTTP 200 OK (La colección ha sido actualizada)
            return ResponseEntity.ok(resultado);

        } catch (NoSuchElementException e) {
            // Si no se encuentra la colección o el hecho (404 Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            // Para cualquier otro error no esperado (500 Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
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

    @PutMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<Void> setearAlgoritmoPorNombre(
            @PathVariable Long id,
            @RequestParam String tipo) {
        try {
            var t = ar.edu.utn.frba.server.contratos.enums.TipoAlgoritmoConsenso.fromCodigo(tipo);

            ar.edu.utn.frba.server.servicioAgregador.domain.consenso.AlgoritmoDeConsensoStrategy algoritmo = switch (t) {
                case MAYORIA_SIMPLE       -> new ar.edu.utn.frba.server.servicioAgregador.domain.consenso.MayoriaSimpleStrategy();
                case MULTIPLES_MENCIONES  -> new ar.edu.utn.frba.server.servicioAgregador.domain.consenso.MultiplesMencionesStrategy();
                case ABSOLUTA             -> new ar.edu.utn.frba.server.servicioAgregador.domain.consenso.AbsolutaStrategy();
                case DEFECTO              -> new ar.edu.utn.frba.server.servicioAgregador.domain.consenso.ConsensoPorDefectoStrategy();
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

    @PatchMapping("/colecciones/{coleccionId}/hechos/{hechoId}/agregar-fuente")
    public ResponseEntity<?> agregarFuenteAHecho(
            @PathVariable Long coleccionId,
            @PathVariable Long hechoId,
            @RequestParam Fuente tipoFuente) {
        try {
            Hecho actualizado = coleccionService.agregarFuenteAHecho(coleccionId, hechoId, tipoFuente);

            // devolver DTO para evitar problemas de serialización (Optional, etc.)
            var dto = ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto.fromModel(actualizado);
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
            var dto = ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto.fromModel(actualizado);
            return ResponseEntity.ok(dto);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
