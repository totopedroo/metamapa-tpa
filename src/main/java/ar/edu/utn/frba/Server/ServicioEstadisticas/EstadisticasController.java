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


    private ResponseEntity<String> csv(String filename, String body) {
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.valueOf("text/csv"))
                .body(body);
    }

    @GetMapping(value = "/colecciones/{id}/provincia-top.csv", produces = "text/csv")
    public ResponseEntity<String> provinciaTopCsv(@PathVariable String id) {
        var top = svc.provinciaTop(id);
        String csv = "provincia,total\n" + safe(top.clave()) + "," + top.valor() + "\n";
        return csv("provincia-top-" + id + ".csv", csv);
    }

    @GetMapping(value = "/colecciones/{id}/categoria-top.csv", produces = "text/csv")
    public ResponseEntity<String> categoriaTopCsv(@PathVariable String id) {
        var top = svc.categoriaTop(id);
        String csv = "categoria,total\n" + safe(top.clave()) + "," + top.valor() + "\n";
        return csv("categoria-top-" + id + ".csv", csv);
    }

    @GetMapping(value = "/colecciones/{id}/provincia-top-por-categoria.csv", produces = "text/csv")
    public ResponseEntity<String> provinciaTopPorCategoriaCsv(@PathVariable String id,
                                                              @RequestParam String categoria) {
        var top = svc.provinciaTopPorCategoria(id, categoria);
        String csv = "provincia,total\n" + safe(top.clave()) + "," + top.valor() + "\n";
        return csv("provincia-top-por-categoria-" + id + ".csv", csv);
    }

    @GetMapping(value = "/colecciones/{id}/hora-top-por-categoria.csv", produces = "text/csv")
    public ResponseEntity<String> horaTopPorCategoriaCsv(@PathVariable String id,
                                                         @RequestParam String categoria) {
        var top = svc.horaTopPorCategoria(id, categoria);
        String csv = "hora,total\n" + safe(top.clave()) + "," + top.valor() + "\n";
        return csv("hora-top-por-categoria-" + id + ".csv", csv);
    }

    @GetMapping(value = "/solicitudes/spam.csv", produces = "text/csv")
    public ResponseEntity<String> solicitudesSpamCsv() {
        long n = svc.solicitudesSpam();
        String csv = "metric,value\nsolicitudes_spam," + n + "\n";
        return csv("solicitudes-spam.csv", csv);
    }

    private String safe(String s) {
        if (s == null) return "";
        // reemplaza comas y saltos para no romper el CSV (simple)
        return s.replace(",", " ").replace("\r", " ").replace("\n", " ").trim();
    }
}
