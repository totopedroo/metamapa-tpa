package ar.edu.utn.frba.server.servicioAgregador.controllers;
import ar.edu.utn.frba.server.common.enums.EstadoRevisionHecho;
import ar.edu.utn.frba.server.fuente.estatica.services.FuenteEstaticaService;
import ar.edu.utn.frba.server.servicioAgregador.algoritmos.consenso.*;
import ar.edu.utn.frba.server.servicioAgregador.domain.*;
import ar.edu.utn.frba.server.servicioAgregador.dtos.ColeccionInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IColeccionRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.ISolicitudRepository;
import ar.edu.utn.frba.server.servicioAgregador.services.*;
import ar.edu.utn.frba.server.servicioAgregador.dtos.ColeccionOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.algoritmos.navegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.server.servicioEstadisticas.services.ExportacionCSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.security.SecureRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@CrossOrigin("http://localhost:8080")
public class AdminController {
    private final ImportadorCSV importadorCSV;
    private final SecureRandom secureRandom = new SecureRandom();
    @Autowired
    private IColeccionService coleccionService;
    @Autowired
    private IHechosService hechosService;
    @Autowired
    private IHechosRepository hechosRepository;
    @Autowired
    private ISolicitudRepository solicitudRepository;
    @Autowired
    private IColeccionRepository coleccionRepository;
    @Autowired
    private FuenteEstaticaService fuenteEstaticaService;
    @Autowired
    private ExportacionCSVService exportacionCSVService;

    public AdminController(ImportadorCSV importadorCSV, IColeccionService coleccionService) {
        this.importadorCSV = importadorCSV;
        this.coleccionService = coleccionService;
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

    @PutMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<Void> setearAlgoritmoPorNombre(
            @PathVariable Long id,
            @RequestParam String tipo) {
        try {
            var t = ar.edu.utn.frba.server.common.enums.TipoAlgoritmoConsenso.fromCodigo(tipo);

            AlgoritmoDeConsensoStrategy algoritmo = switch (t) {
                case MAYORIA_SIMPLE       -> new MayoriaSimpleStrategy();
                case MULTIPLES_MENCIONES  -> new MultiplesMencionesStrategy();
                case ABSOLUTA             -> new AbsolutaStrategy();
                case DEFECTO              -> new ConsensoPorDefectoStrategy();
            };

            coleccionService.setAlgoritmoDeConsenso(id, algoritmo);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sincronizar")
    public ResponseEntity<List<Hecho>> sincronizarFuenteEstatica() {
        try {
            List<Hecho> hechos = fuenteEstaticaService.sincronizar();
            return ResponseEntity.ok(hechos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/crearDesdeCSV")
    public ResponseEntity<ColeccionOutputDto> crearDesdeCSV(@RequestParam String path) {
        var c = coleccionService.crearColeccionDesdeFuentes("COLECCION CSV", path);
        return ResponseEntity.ok(ColeccionOutputDto.fromModel(c));
    }

    @PostMapping("/colecciones")
    public ResponseEntity<ColeccionOutputDto> crearColeccion(@RequestBody ColeccionInputDto dto) {
        var out = coleccionService.crearColeccionManual(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    @PatchMapping("/colecciones/{id}")
    public ResponseEntity<ColeccionOutputDto> editarColeccion(@PathVariable Long id, @RequestBody ColeccionInputDto dto) {
        try {
            var out = coleccionService.editarColeccion(id, dto);
            return ResponseEntity.ok(out);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable Long id) {
        try {
            coleccionService.eliminarColeccion(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        Map<String, Object> data = new HashMap<>();
        data.put("hechos", hechosRepository.countActivos());
        data.put("colecciones", coleccionRepository.countActivas());
        data.put("solicitudesPendientes", solicitudRepository.countPendientes());
        data.put("hechosPendientes", hechosRepository.countPendientes());
        return ResponseEntity.ok(data);
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
            @PathVariable Long hechoId,
            @RequestParam Fuente tipoFuente) {
        try {
            Hecho actualizado = coleccionService.quitarFuenteDeHecho(coleccionId, hechoId, tipoFuente);

            // Devolver DTO para no chocar con Optional/JSON
            var dto = ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto.fromModel(actualizado);
            return ResponseEntity.ok(dto);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/hechos/{id}/revision")
    public ResponseEntity<HechosOutputDto> revisarHecho(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String accion = body.get("accion");
        if (accion == null) return ResponseEntity.badRequest().build();

        try {
            HechosOutputDto out = switch (accion.toLowerCase()) {
                case "aprobar" -> hechosService.aprobarHecho(id);
                case "rechazar" -> hechosService.rechazarHecho(id);
                case "modificar", "aceptar_modificado" -> hechosService.aceptarConModificaciones(id);
                default -> throw new IllegalArgumentException("Acción inválida: " + accion);
            };
            return ResponseEntity.ok(out);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/hechos/revision")
    public ResponseEntity<List<HechosOutputDto>> obtenerHechosARevisar() {
        List<HechosOutputDto> pendientes = hechosService.obtenerHechosPendientesDeRevision();

        if (pendientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pendientes);
    }
}
