package ar.edu.utn.frba.server.fuente.dinamica.controllers;

import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosInputDto;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.fuente.dinamica.services.IFuenteDinamicaService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fuente-dinamica")
@CrossOrigin("http://localhost:8080")
@RequiredArgsConstructor
public class FuenteDinamicaController {

    private final IFuenteDinamicaService fuenteDinamicaService;

    @PostMapping("/hechos")
    public ResponseEntity<HechosOutputDto> crearHecho(@RequestBody HechosInputDto hechoInputDto) {
        HechosOutputDto created = fuenteDinamicaService.crearHecho(hechoInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/hechos/{hechoId}")
    public ResponseEntity<HechosOutputDto> editarHecho(@PathVariable Long hechoId,
                                                       @RequestBody HechosOutputDto out) {
        HechosOutputDto edited = fuenteDinamicaService.editarHecho(hechoId, out);
        return ResponseEntity.ok(edited);
    }

    @GetMapping("/estado")
    public String estado() { return "Fuente Dinámica operativa"; }
}

