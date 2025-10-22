package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.common.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.services.ISolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin("http://localhost:8080")
public class SolicitudController {

    @Autowired
    private ISolicitudService solicitudService;

    @PostMapping("/solicitudes")
    public ResponseEntity<SolicitudOutputDto> crearSolicitud(@RequestBody SolicitudInputDto dto) {
        try {
            SolicitudOutputDto salida = solicitudService.crearSolicitud(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salida);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new SolicitudOutputDto("No se encontró el hecho con ID: " + dto.getIdHecho()));
        }
    }

    @PatchMapping("/solicitudes/{id}")
    public ResponseEntity<String> actualizarEstadoSolicitud(
            @PathVariable Long id, @RequestBody Map<String, String> body) {

        String raw = body.get("estado");
        if (raw == null) return ResponseEntity.badRequest().body("Falta 'estado'. Los estados pueden ser 'ACEPTADA' | 'RECHAZADA'.");

        try {
            EstadoDeSolicitud nuevo = EstadoDeSolicitud.valueOf(raw.trim().toUpperCase());
            switch (nuevo) {
                case ACEPTADA -> { solicitudService.aceptarSolicitud(id); return ResponseEntity.ok("Solicitud aceptada"); }
                case RECHAZADA -> { solicitudService.rechazarSolicitud(id); return ResponseEntity.ok("Solicitud rechazada"); }
                default -> { return ResponseEntity.badRequest().body("Estado no válido"); }
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado inválido. Use: ACEPTADA | RECHAZADA");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
