package ar.edu.utn.frba.Server.ServicioEstadisticas;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {


    private final EstadisticasService estadisticasService;

    public EstadisticasController( EstadisticasService estadisticasService) {

        this.estadisticasService = estadisticasService;
    }

    private ResponseEntity<String> csv(String filename, String body) {
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.valueOf("text/csv"))
                .body(body);
    }

    @GetMapping("/colecciones/{id}/provincia-top")
    public ResponseEntity<?> provinciaTop(@PathVariable String id) {
        return ResponseEntity.ok(estadisticasService.provinciaTop(id));
    }

    @GetMapping("/colecciones/{id}/categoria-top")
    public ResponseEntity<?> categoriaTop(@PathVariable String id) {
        return ResponseEntity.ok(estadisticasService.categoriaTop(id));
    }

    @GetMapping("/colecciones/{id}/provincia-top-por-categoria")
    public ResponseEntity<?> provinciaTopPorCategoria(@PathVariable String id,
                                                      @RequestParam String categoria) {
        return ResponseEntity.ok(estadisticasService.provinciaTopPorCategoria(id, categoria));
    }

    @GetMapping("/colecciones/{id}/hora-top-por-categoria")
    public ResponseEntity<?> horaTopPorCategoria(@PathVariable String id,
                                                 @RequestParam String categoria) {
        return ResponseEntity.ok( estadisticasService.horaTopPorCategoria(id, categoria));
    }

    @GetMapping("/solicitudes/spam")
    public ResponseEntity<?> solicitudesSpam() {
        return ResponseEntity.ok(estadisticasService.solicitudesSpam());
    }

    @GetMapping(value = "/colecciones/{id}/resumen.csv", produces = "text/csv")
    public ResponseEntity<String> resumenCsv(@PathVariable String id,
                                             @RequestParam(required = false) String categoria) {
        var provTop = estadisticasService.provinciaTop(id);
        var catTop  = estadisticasService.categoriaTop(id);

        StringBuilder sb = new StringBuilder();
        sb.append(CsvExporter.row("metric", "key", "value"));

        sb.append(CsvExporter.row("provincia_top", provTop.clave(), String.valueOf(provTop.valor())));
        sb.append(CsvExporter.row("categoria_top", catTop.clave(), String.valueOf(catTop.valor())));

        if (categoria != null && !categoria.isBlank()) {
            var provCat = estadisticasService.provinciaTopPorCategoria(id, categoria);
            var horaCat = estadisticasService.horaTopPorCategoria(id, categoria);
            sb.append(CsvExporter.row("provincia_top_por_categoria(" + categoria + ")", provCat.clave(), String.valueOf(provCat.valor())));
            sb.append(CsvExporter.row("hora_top_por_categoria(" + categoria + ")", horaCat.clave(), String.valueOf(horaCat.valor())));
        }


        long spam = estadisticasService.solicitudesSpam();
        sb.append(CsvExporter.row("solicitudes_spam", "", String.valueOf(spam)));

        return csv("resumen-" + id + ".csv", sb.toString());
    }
}


