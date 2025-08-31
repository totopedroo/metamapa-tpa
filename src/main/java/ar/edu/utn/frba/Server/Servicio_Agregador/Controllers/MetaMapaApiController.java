package ar.edu.utn.frba.Server.Servicio_Agregador.Controllers;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.SolicitudEliminacion;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.SolicitudInputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.SolicitudOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.ColeccionRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.IHechosRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.IColeccionService;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ISolicitudService;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.IHechosService;

import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.CuradaStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.ModoNavegacionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:8080")
public class MetaMapaApiController {

    @Autowired
    @Qualifier("hechosAgregadorService")
    private IHechosService hechosService;

    @Autowired
    private IHechosRepository hechosRepository;
    @Autowired
    @Qualifier("coleccionService")
    private IColeccionService coleccionService;

    @Autowired
    @Qualifier("solicitudAgregadorService")
    private ISolicitudService solicitudService;
    @Autowired
    private ColeccionRepository coleccionRepository;


    @GetMapping("/{id}/hechos")
    public List<Hecho> obtenerHechosColeccion(@PathVariable String id) {
        return coleccionService.obtenerHechosPorColeccion(id);
    }

    @GetMapping("/colecciones")
    public ResponseEntity<List<Coleccion>> obtenerColecciones() {
        try {
            List<Coleccion> colecciones = coleccionService.findAll();
            return ResponseEntity.ok(colecciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/colecciones/{identificador}/hechos")
    public ResponseEntity<List<Hecho>> obtenerHechosDeColeccion(@PathVariable String identificador) {
        try {
            List<Hecho> hechos = coleccionService.obtenerHechosPorColeccion(identificador);
            return ResponseEntity.ok(hechos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
/*
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
*/
    //todo
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


    @PostMapping("/hechos/{id}/crearSolicitud")
    public ResponseEntity<String> solicitarEliminacion(@PathVariable Long id, @RequestBody String motivo) {
        Hecho hecho = hechosRepository.findById(id);
        if (hecho == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hecho no encontrado");
        }

        SolicitudEliminacion nuevaSolicitud = new SolicitudEliminacion(motivo, id);
        hecho.agregarSolicitud(nuevaSolicitud);
        hechosRepository.save(hecho);

        return ResponseEntity.ok("Solicitud de eliminación generada.");
    }



    @GetMapping("/estado")
    public ResponseEntity<String> obtenerEstado() {
        return ResponseEntity.ok("MetaMapa API operativa");
    }

}

