package ar.edu.utn.frba.Servicio_Agregador.Controllers;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.SolicitudInputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.SolicitudOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Repository.ColeccionRepository;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AbsolutaStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.MayoriaSimpleStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.MultiplesMencionesStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.IColeccionService;
import ar.edu.utn.frba.Servicio_Agregador.Service.ISolicitudService;
import ar.edu.utn.frba.Servicio_Agregador.Service.IHechosService;

import ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion.CuradaStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion.ModoNavegacionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * API pública para otras instancias de MetaMapa
 * Implementa los endpoints especificados en la consigna de la Entrega 2
 */
@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:8080")
public class MetaMapaApiController {

    @Autowired
    @Qualifier("hechosAgregadorService")
    private IHechosService hechosService;

    @Autowired
    @Qualifier("coleccionService")
    private IColeccionService coleccionService;

    @Autowired
    @Qualifier("solicitudAgregadorService")
    private ISolicitudService solicitudService;
    @Autowired
    private ColeccionRepository coleccionRepository;

    /**
     * GET /hechos
     * Expone todos los hechos del sistema y los devuelve como una lista en formato
     * JSON.
     * Acepta parámetros para filtrar los resultados.
     */
    @GetMapping("/hechos")
    public ResponseEntity<List<HechosOutputDto>> obtenerHechos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteHasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoHasta,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud) {
        try {
            List<HechosOutputDto> hechos = hechosService.filtrarHechos(
                    categoria,
                    fechaReporteDesde,
                    fechaReporteHasta,
                    fechaAcontecimientoDesde,
                    fechaAcontecimientoHasta,
                    latitud,
                    longitud);
            return ResponseEntity.ok(hechos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /colecciones
     * Expone todas las colecciones disponibles en esta instancia de MetaMapa,
     * independientemente del origen de sus fuentes.
     */
    @GetMapping("/colecciones")
    public ResponseEntity<List<ColeccionOutputDto>> obtenerColecciones() {
        try {
            List<ColeccionOutputDto> colecciones = coleccionService.buscarTodos();
            return ResponseEntity.ok(colecciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /colecciones/:identificador/hechos
     * Permite obtener los hechos asociados a una colección.
     * Acepta los mismos parámetros que /hechos y devuelve los resultados en el
     * mismo formato.
     */
    @GetMapping("/colecciones/{identificador}/hechos")
    public ResponseEntity<List<HechosOutputDto>> obtenerHechosDeColeccion(@PathVariable String identificador) {
        try {
            List<HechosOutputDto> hechos = coleccionService.obtenerHechosPorColeccion(identificador);
            return ResponseEntity.ok(hechos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /solicitudes
     * Permite crear solicitudes de eliminación, enviando los datos de la solicitud
     * como un JSON a través del cuerpo (body) de la misma.
     */
    @PostMapping("/solicitudes")
    public ResponseEntity<SolicitudOutputDto> crearSolicitudEliminacion(
            @RequestBody SolicitudInputDto solicitudInputDto) {
        try {
            SolicitudOutputDto nuevaSolicitud = solicitudService.crearSolicitud(solicitudInputDto);
            return new ResponseEntity<>(nuevaSolicitud, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /colecciones/{id}/hechos/navegacion
     * Endpoint para navegación curada o irrestricta sobre una colección
     */
    @GetMapping("/colecciones/{id}/hechos/navegacion")
    public ResponseEntity<List<HechosOutputDto>> navegarHechosDeColeccion(
            @PathVariable String id,
            @RequestParam(defaultValue = "irrestricta") String modo) {
        try {
            Coleccion coleccion = coleccionRepository.findById(id);

            ModoNavegacionStrategy estrategia = switch (modo.toLowerCase()) {
                case "curada" -> new CuradaStrategy(coleccion.getAlgoritmoDeConsenso());
                case "irrestricta" -> new IrrestrictaStrategy();
                default -> throw new IllegalArgumentException("Modo de navegación inválido: " + modo);
            };

            List<HechosOutputDto> hechos = coleccionService.navegarHechos(id, estrategia);
            return ResponseEntity.ok(hechos);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para verificar el estado de la API
     */
    @GetMapping("/estado")
    public ResponseEntity<String> obtenerEstado() {
        return ResponseEntity.ok("MetaMapa API operativa");
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

}

