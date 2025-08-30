package ar.edu.utn.frba.Server.Servicio_Agregador.Controllers;

import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.SolicitudInputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.SolicitudOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ISolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/solicitudes")
@CrossOrigin("http://localhost:8080")
public class SolicitudController {

    @Autowired
    private ISolicitudService solicitudService;
//TODO
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

    @PatchMapping("/{id}")
    public ResponseEntity<String> actualizarEstadoSolicitud(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");

        try {
            if ("ACEPTADA".equalsIgnoreCase(nuevoEstado)) {
                solicitudService.aceptarSolicitud(id);
                return ResponseEntity.ok("Solicitud aceptada");
            } else if ("RECHAZADA".equalsIgnoreCase(nuevoEstado)) {
                solicitudService.rechazarSolicitud(id);
                return ResponseEntity.ok(" Solicitud rechazada");
            } else {
                return ResponseEntity.badRequest().body("Estado no válido. Debe ser 'ACEPTADA' o 'RECHAZADA'");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
