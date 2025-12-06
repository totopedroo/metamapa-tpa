package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.*;
import ar.edu.utn.frba.server.servicioAgregador.dtos.*;
import ar.edu.utn.frba.server.servicioAgregador.mappers.ColeccionFrontMapper;
import ar.edu.utn.frba.server.servicioAgregador.mappers.HechoFrontMapper;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IAlgoritmoConsensoRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IColeccionRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IFuenteRepository;
import ar.edu.utn.frba.server.servicioAgregador.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/colecciones")
@RequiredArgsConstructor
public class ColeccionController {

    private final IColeccionService coleccionService;
    private final SolicitudService solicitudService;
    private final ExportacionCSVService exportacionCSVService;

    private final IAlgoritmoConsensoRepository algoritmoRepo;
    private final IColeccionRepository coleccionRepo;
    private final IFuenteRepository fuenteRepo;

    /* =====================================================
       ===============  LANDING PAGE  =======================
       ===================================================== */

    @GetMapping("/ultimas")
    public ResponseEntity<List<ColeccionFrontDto>> listarUltimas() {
        return ResponseEntity.ok(
                coleccionService.listarUltimas()
                        .stream()
                        .map(ColeccionFrontMapper::toDto)
                        .toList()
        );
    }

    /* =====================================================
       =========   ALGORITMOS DE CONSENSO   ================
       ===================================================== */

    @GetMapping("/algoritmos")
    public ResponseEntity<List<AlgoritmoConsenso>> listarAlgoritmosDisponibles() {
        return ResponseEntity.ok(algoritmoRepo.findAll());
    }

    @GetMapping("/algoritmos/buscar")
    public ResponseEntity<Long> buscarAlgoritmoPorNombre(@RequestParam String nombre) {
        var algo = algoritmoRepo.findByNombre(nombre)
                .orElse(null);

        return ResponseEntity.ok(algo != null ? algo.getId() : null);
    }

    @PutMapping("/{id}/asociar-algoritmo")
    public ResponseEntity<?> asociarAlgoritmo(
            @PathVariable Long id,
            @RequestParam(required = false) Long algoritmoId) {
        try {
            Coleccion c = coleccionRepo.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Colección no encontrada"));

            if (algoritmoId != null) {
                AlgoritmoConsenso algo = algoritmoRepo.findById(algoritmoId)
                        .orElseThrow(() -> new NoSuchElementException("Algoritmo no encontrado"));
                c.setAlgoritmoConsensoEntidad(algo);
            } else {
                c.setAlgoritmoConsensoEntidad(null);
            }

            coleccionRepo.save(c);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    /* =====================================================
       ================   CRUD PRINCIPAL   ==================
       ===================================================== */

    @GetMapping
    public ResponseEntity<List<ColeccionFrontDto>> listar() {
        var out = coleccionService.findAll().stream()
                .map(ColeccionFrontMapper::toDto)
                .toList();
        return ResponseEntity.ok(out);
    }


    @PostMapping
    public ResponseEntity<ColeccionFrontDto> crear(
            @Valid @RequestBody ColeccionInputBD dto) {
            System.out.println(">>> LLEGO AL BACKEND");
            System.out.println("TITULO: " + dto.getTitulo());
            System.out.println("DESC: " + dto.getDescripcion());
            System.out.println("ADMIN: " + dto.getAdministradorId()); // <-- acá vas a ver si llega NULL
            System.out.println("HECHOS: " + dto.getHechosIds());
            System.out.println("CRITERIOS: " + dto.getCriteriosIds());


            var creada = coleccionService.crear(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ColeccionFrontMapper.toDto(creada));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> listarUna(@PathVariable Long id) {
        try {
            var coleccion = coleccionService.listar(id);
            var dto = ColeccionFrontMapper.toDto(coleccion);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity<ColeccionFrontDto> editar(
            @PathVariable Long id,
            @RequestBody ColeccionUpdateBD in) {

        var updated = coleccionService.editar(id, in);
        return ResponseEntity.ok(ColeccionFrontMapper.toDto(updated));
    }


    @PostMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        coleccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    /* =====================================================
       ============   HECHOS DE COLECCIÓN   =================
       ===================================================== */

    @GetMapping("/{id}/hechos")
    public ResponseEntity<List<HechoFrontDto>> obtenerHechosDeColeccion(@PathVariable Long id) {

        var hechos = coleccionService.obtenerHechosPorColeccion(id)
                .stream()
                .map(HechoFrontMapper::toDto)
                .toList();

        return ResponseEntity.ok(hechos);
    }


    @GetMapping("/{id}/hechos/navegacion")
    public ResponseEntity<List<HechoFrontDto>> navegarHechos(
            @PathVariable Long id,
            @RequestParam(defaultValue = "irrestricta") String modo) {

        var hechos = coleccionService.navegarHechos(id, modo)
                .stream()
                .map(HechoFrontMapper::toDto)
                .toList();

        return ResponseEntity.ok(hechos);
    }


    @PatchMapping("/{coleccionId}/hechos/{hechoId}")
    public ResponseEntity<ColeccionFrontDto> agregarHechoAColeccion(
            @PathVariable Long coleccionId,
            @PathVariable Long hechoId) {

        try {
            var resultado = coleccionService.agregarHechoAColeccion(coleccionId, hechoId);
            return ResponseEntity.ok(ColeccionFrontMapper.toDto(resultado));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }



    /* =====================================================
       =================  SOLICITUDES  ======================
       ===================================================== */

    @PostMapping("/solicitudes/{id}/aprobar")
    public ResponseEntity<Void> aprobarSolicitud(@PathVariable Long id) {
        solicitudService.aceptarSolicitud(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/solicitudes/{id}/denegar")
    public ResponseEntity<Void> denegarSolicitud(@PathVariable Long id) {
        solicitudService.rechazarSolicitud(id);
        return ResponseEntity.ok().build();
    }



    /* =====================================================
       =================  CSV + CONSENSO  ==================
       ===================================================== */

    @PostMapping("/crearDesdeCSV")
    public ResponseEntity<ColeccionFrontDto> crearDesdeCSV(@RequestParam String path) {
        var c = coleccionService.crearColeccionDesdeFuentes("COLECCION CSV", path);
        return ResponseEntity.ok(ColeccionFrontMapper.toDto(c));
    }

    @PatchMapping("/{coleccionId}/hechos/{hechoId}/consensuar")
    public ResponseEntity<HechoFrontDto> consensuarHecho(
            @PathVariable Long coleccionId,
            @PathVariable Long hechoId) {

        var out = coleccionService.consensuarHecho(coleccionId, hechoId);
        return ResponseEntity.ok(HechoFrontMapper.toDto(out));
    }

    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importarCsv(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println(">>> ¡LLEGUÉ AL CONTROLLER DEL BACKEND! <<<"); // <--- Agrega esto
            System.out.println("Archivo recibido: " + file.getOriginalFilename());
            coleccionService.importarDesdeWeb(file);
            return ResponseEntity.ok("Importación finalizada con éxito.");
        } catch (Exception e) {
            System.out.println(">>> ¡Rompi porque soy re puto! <<<");
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /* =====================================================
       =========   CONFIGURACIÓN DE FUENTES   ==============
       ===================================================== */

    // 1. Listar fuentes disponibles para el select
    @GetMapping("/fuentes")
    public ResponseEntity<List<Fuente>> listarFuentesDisponibles() {
        return ResponseEntity.ok(fuenteRepo.findAll());
    }

    @GetMapping("/fuentes/buscar")
    public ResponseEntity<Long> buscarFuentePorTipo(@RequestParam String tipo) {
        try {
            var tipoEnum = TipoFuente.valueOf(tipo); // CSV, API, MANUAL, etc.

            var fuente = fuenteRepo.findByTipo(tipoEnum)
                    .orElse(null);

            return ResponseEntity.ok(fuente != null ? fuente.getId() : null);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(null); // tipo inválido
        }
    }

    // 2. Asociar fuente a colección
    @PutMapping("/{id}/asociar-fuente")
    public ResponseEntity<?> asociarFuente(
        @PathVariable Long id,
        @RequestParam(required = false) Long fuenteId) {
        try {
            Coleccion c = coleccionRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Colección no encontrada"));

            if (fuenteId != null) {
                Fuente fuente = fuenteRepo.findById(fuenteId)
                    .orElseThrow(() -> new NoSuchElementException("Fuente no encontrada"));
                c.setFuente(fuente);
            } else {
                c.setFuente(null);
            }

            coleccionRepo.save(c);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }
    }

}