package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.servicioAgregador.domain.Coleccion;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudEliminacion;
import ar.edu.utn.frba.server.servicioAgregador.domain.navegacion.CuradaStrategy;
import ar.edu.utn.frba.server.servicioAgregador.domain.navegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.server.servicioAgregador.domain.navegacion.ModoNavegacionStrategy;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IColeccionRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.services.IColeccionService;
import ar.edu.utn.frba.server.servicioAgregador.services.ISolicitudService;
import ar.edu.utn.frba.server.servicioAgregador.services.IHechosService;

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
    private IColeccionRepository coleccionRepository;


    @GetMapping("/{id}/hechos")
    public List<Hecho> obtenerHechosColeccion(@PathVariable Long id) {
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
    public ResponseEntity<List<Hecho>> obtenerHechosDeColeccion(@PathVariable Long identificador) {
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
            @PathVariable Long id,
            @RequestParam(defaultValue = "irrestricta") String modo) {
        try {
            Coleccion coleccion = coleccionRepository.findById(id).orElse(null);
            if (coleccion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
            }
            ModoNavegacionStrategy estrategia = switch (modo.toLowerCase()) {
                case "curada" -> new CuradaStrategy();
                case "irrestricta" -> new IrrestrictaStrategy();
                default -> throw new IllegalArgumentException("Modo de navegación inválido: " + modo);
            };

            List<Hecho> hechos = coleccionService.navegarHechos(id, (String) estrategia);
            return ResponseEntity.ok(hechos);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
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

    @GetMapping("/colecciones/{coleccionId}/hechos/filtrados")
    public ResponseEntity<List<Hecho>> filtrarHechosPorColeccion(
            @PathVariable Long coleccionId,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String categoria
    ) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId).orElse(null);

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
        Hecho hecho = hechosRepository.findById(id).orElse(null);
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

