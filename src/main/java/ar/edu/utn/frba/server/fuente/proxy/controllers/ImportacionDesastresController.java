package ar.edu.utn.frba.server.fuente.proxy.controllers;

import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import ar.edu.utn.frba.server.fuente.proxy.services.ImportacionDesastresService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/fuente-dinamica/desastres")
@RequiredArgsConstructor
public class ImportacionDesastresController {

    private final ImportacionDesastresService service;

    /**
     * GET: ver lo que devuelve la API (no persiste).
     */
    @GetMapping("/preview")
    public ResponseEntity<List<Hecho>> preview() {
        return ResponseEntity.ok(service.preview());
    }

    /**
     * POST: trae y guarda en la tabla `hecho`.
     */
    @PostMapping("/sincronizar")
    public ResponseEntity<List<Hecho>> sincronizar() {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.sincronizar());
    }
}