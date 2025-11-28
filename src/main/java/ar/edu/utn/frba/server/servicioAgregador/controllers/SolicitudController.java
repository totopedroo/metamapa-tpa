package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudFrontDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.services.ISolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitudes")
@CrossOrigin(origins = "http://localhost:8082")
public class SolicitudController {

    @Autowired
    private ISolicitudService solicitudService;

    @PostMapping("")
    public ResponseEntity<SolicitudOutputDto> crearSolicitud(@RequestBody SolicitudInputDto dto) {
        try {
            SolicitudOutputDto salida = solicitudService.crearSolicitud(dto);
            return ResponseEntity.ok(salida);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new SolicitudOutputDto("No se encontró el hecho con ID: " + dto.getIdHecho()));
        }
    }

    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long idUsuario) {
        try {
            List<SolicitudFrontDto> lista = solicitudService.obtenerPorUsuario(idUsuario);
            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/admin")
    public ResponseEntity<?> obtenerSolicitudes() {
        try {
            List<SolicitudFrontDto> lista = solicitudService.obtenerSolicitudesConTitulo();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en el servidor: " + e.getMessage());
        }
    }


    @PostMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarSolicitud(@PathVariable Long id) {
        try {
            solicitudService.aceptarSolicitud(id);
            return ResponseEntity.ok("Solicitud aprobada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al aprobar: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarSolicitud(@PathVariable Long id) {
        try {
            solicitudService.rechazarSolicitud(id);
            return ResponseEntity.ok("Solicitud rechazada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al rechazar: " + e.getMessage());
        }
    }
}
