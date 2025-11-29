package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudModificacionInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.services.ISolicitudService;
import ar.edu.utn.frba.server.servicioAgregador.services.IsolicitudModificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitudes-modificacion") // Recomiendo usar prefijos claros
@CrossOrigin(origins = "http://localhost:8080") // O el puerto de tu front
@RequiredArgsConstructor
public class SolicitudModificacionController {

    private final IsolicitudModificacionService solicitudService;

    // 1. CREAR SOLICITUD (Cualquier usuario)
    @PostMapping
    public ResponseEntity<?> crearSolicitud(@RequestBody SolicitudModificacionInputDto dto) {
        try {
            solicitudService.crearSolicitud(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Solicitud de modificación creada.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Datos inválidos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la solicitud: " + e.getMessage());
        }
    }

    // 2. APROBAR SOLICITUD (Solo Admin - Cambia el valor real en el Hecho)
    @PostMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarSolicitud(@PathVariable Long id) {
        try {
            solicitudService.aprobarSolicitud(id);
            return ResponseEntity.ok("Solicitud aprobada y cambio aplicado en el Hecho.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al aprobar: " + e.getMessage());
        }
    }

    // 3. RECHAZAR SOLICITUD (Solo Admin)
    @PostMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarSolicitud(@PathVariable Long id) {
        try {
            // Asumiendo que agregaste este método en el service (es solo cambiar estado)
            solicitudService.rechazarSolicitud(id);
            return ResponseEntity.ok("Solicitud rechazada.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al rechazar: " + e.getMessage());
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<?> findAllPendientes(){
        try{
            return ResponseEntity.ok(solicitudService.findAllPendientes());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obterner todas las solicitudes de modificacion: " + e.getMessage());
        }
    }
}