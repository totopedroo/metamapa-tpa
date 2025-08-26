package ar.edu.utn.frba.Server.Servicio_Agregador.Controllers;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.TipoFuente;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.HechosService;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.ColeccionRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.HechosRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso.*;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.IColeccionService;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ISeederService;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.CuradaStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.ModoNavegacionStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.SolicitudService;
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
public class ColeccionController {

    private final SecureRandom secureRandom = new SecureRandom();
    @Autowired
    private IColeccionService coleccionService;
    @Autowired
    private ISeederService seederService;
    @Autowired
    private ColeccionRepository coleccionRepository;
    @Autowired
    private IrrestrictaStrategy irrestrictaStrategy;

    @Autowired
    private HechosRepository hechosRepository;
    @Autowired
    private HechosService hechosService;
    @Autowired
    private SolicitudService solicitudService;


    @GetMapping("/colecciones")
    public List<ColeccionOutputDto> getColecciones() {
        return coleccionService.buscarTodos();
    }

    @GetMapping("/{id}/hechos")
    public List<Hecho> obtenerHechosDeColeccion(@PathVariable String id) {
        return coleccionService.obtenerHechosPorColeccion(id);
    }

    @GetMapping("/createColeccionAPI")
    public Coleccion crearColeccionPruebaAPI() {
        return coleccionService.setColeccionApi();
    }

    @GetMapping("/createColeccionCSV")
        public Coleccion crearColeccionPruebaCSV() {
            return coleccionService.setColeccionCsv();
        }

    @PostMapping("/colecciones/{coleccionId}/hechos/{hechoId}")
    public ResponseEntity<?> agregarHechoAColeccion(@PathVariable String coleccionId, @PathVariable Long hechoId) {
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
    public ResponseEntity<List<Hecho>> navegarHechos(
            @PathVariable String id,
            @RequestParam(defaultValue = "irrestricta") String modo) {
        try {
            Coleccion coleccion = coleccionRepository.findById(id);
            if (coleccion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
            }
            ModoNavegacionStrategy estrategia = switch (modo.toLowerCase()) {
                case "curada" -> new CuradaStrategy();
                case "irrestricta" -> new IrrestrictaStrategy();
                default -> throw new IllegalArgumentException("Modo de navegación inválido: " + modo);
            };

            List<Hecho> hechos = coleccionService.navegarHechos(id, estrategia);
            return ResponseEntity.ok(hechos);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PutMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<Void> setearAlgoritmoPorNombre(
            @PathVariable String id,
            @RequestParam String tipo) {

        try {
            AlgoritmoDeConsensoStrategy algoritmo = switch (tipo.toLowerCase()) {
                case "mayoriasimple"       -> new MayoriaSimpleStrategy();
                case "multiplesmenciones"  -> new MultiplesMencionesStrategy();
                case "absoluta"            -> new AbsolutaStrategy();
                case "defecto"              -> new ConsensoPorDefectoStrategy();
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
    public ResponseEntity<String> consensuarHecho(
            @PathVariable String coleccionId,
            @PathVariable String hechoId) {

        Coleccion coleccion = coleccionRepository.findById(coleccionId);
        if (coleccion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Colección no encontrada.");
        }

        Optional<Hecho> hechoOpt = coleccion.getHechos().stream()
                .filter(h -> String.valueOf(h.getIdHecho()).equals(hechoId))
                .findFirst();

        if (hechoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hecho no encontrado.");
        }

        Hecho hecho = hechoOpt.get();
        hecho.setConsensuado(Optional.of(true));


        coleccionRepository.save(coleccion);
        return ResponseEntity.ok("Hecho marcado como consensuado.");
    }

    @PatchMapping("/colecciones/{coleccionId}/hechos/{hechoId}/agregar-fuente")
    public ResponseEntity<?> agregarFuenteAHecho(
            @PathVariable String coleccionId,
            @PathVariable Long hechoId,
            @RequestParam TipoFuente tipoFuente) {

        try {
            Coleccion coleccion = coleccionRepository.findById(coleccionId);
            if (coleccion == null) return ResponseEntity.notFound().build();

            Optional<Hecho> hechoOpt = coleccion.getHechos().stream()
                    .filter(h -> h.getIdHecho().equals(hechoId))
                    .findFirst();

            if (hechoOpt.isEmpty()) return ResponseEntity.notFound().build();

            Hecho hecho = hechoOpt.get();
            hecho.setTipoFuente(tipoFuente);
            coleccionRepository.save(coleccion);
            return ResponseEntity.ok("Fuente agregada correctamente al hecho.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/colecciones/{coleccionId}/hechos/{hechoId}/quitar-fuente")
    public ResponseEntity<?> quitarFuenteDeHecho(
            @PathVariable String coleccionId,
            @PathVariable Long hechoId) {

        try {
            Coleccion coleccion = coleccionRepository.findById(coleccionId);
            if (coleccion == null) return ResponseEntity.notFound().build();

            Optional<Hecho> hechoOpt = coleccion.getHechos().stream()
                    .filter(h -> h.getIdHecho().equals(hechoId))
                    .findFirst();

            if (hechoOpt.isEmpty()) return ResponseEntity.notFound().build();

            Hecho hecho = hechoOpt.get();
            hecho.setTipoFuente(null);

            coleccionRepository.save(coleccion);
            return ResponseEntity.ok("Fuente quitada del hecho.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/colecciones/{coleccionId}/hechos/filtrados")
    public ResponseEntity<List<Hecho>> filtrarHechosPorColeccion(
            @PathVariable String coleccionId,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String categoria
    ) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId);

        if (coleccion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }


        List<Hecho> hechosFiltrados = coleccion.getHechos().stream()
                .filter(h -> (titulo == null || h.getTitulo().toLowerCase().contains(titulo.toLowerCase())))
                .filter(h -> (categoria == null || h.getCategoria().equalsIgnoreCase(categoria)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(hechosFiltrados);
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

}

