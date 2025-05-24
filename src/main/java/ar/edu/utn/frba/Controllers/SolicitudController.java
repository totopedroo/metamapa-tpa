package ar.edu.utn.frba.Controllers;

import ar.edu.utn.frba.Dtos.SolicitudOutputDto;
import ar.edu.utn.frba.Dtos.SolicitudInputDto;
import ar.edu.utn.frba.Service.ISolicitudService;
import ar.edu.utn.frba.Service.ISeederService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/solicitudes")
@CrossOrigin("http://localhost:8080")
public class SolicitudController {

    @Autowired
    private ISolicitudService solicitudService;

    @PostMapping
    public ResponseEntity<SolicitudOutputDto> crearSolicitud(@RequestBody SolicitudInputDto dto) {
        try {
            SolicitudOutputDto salida = solicitudService.crearSolicitud(dto);
            return ResponseEntity.ok(salida);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
