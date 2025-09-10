package ar.edu.utn.frba.Server.ServicioEstadisticas;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de estadísticas (JSON + CSV).
 * Requiere el bean EstadisticasService en el mismo package base.
 */
@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {

    private final EstadisticasService svc;

    public EstadisticasController(EstadisticasService svc) {
        this.svc = svc;
    }

    @GetMapping("/colecciones/{id}/provincia-top")
    public ResponseEntity<?> provinciaTop(@PathVariable String id) {
        return ResponseEntity.ok(svc.provinciaTop(id));
    }

    @GetMapping("/colecciones/{id}/categoria-top")
    public ResponseEntity<?> categoriaTop(@PathVariable String id) {
        return ResponseEntity.ok(svc.categoriaTop(id));
    }

    @GetMapping("/colecciones/{id}/provincia-top-por-categoria")
    public ResponseEntity<?> provinciaTopPorCategoria(@PathVariable String id,
                                                      @RequestParam String categoria) {
        return ResponseEntity.ok(svc.provinciaTopPorCategoria(id, categoria));
    }

    @GetMapping("/colecciones/{id}/hora-top-por-categoria")
    public ResponseEntity<?> horaTopPorCategoria(@PathVariable String id,
                                                 @RequestParam String categoria) {
        return ResponseEntity.ok(svc.horaTopPorCategoria(id, categoria));
    }

    @GetMapping("/solicitudes/spam")
    public ResponseEntity<?> solicitudesSpam() {
        return ResponseEntity.ok(svc.solicitudesSpam());
    }

}
