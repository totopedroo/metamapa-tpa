package ar.edu.utn.frba.server.fuente.dinamica.controllers;

import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosInputDto;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.fuente.dinamica.services.IFuenteDinamicaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosInputDto;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.fuente.dinamica.repositories.IHechosDinamicosRepository;
import ar.edu.utn.frba.server.fuente.dinamica.services.FuenteDinamicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuente-dinamica")
@RequiredArgsConstructor
public class FuenteDinamicaController {

    private final FuenteDinamicaService service;
    private final IHechosDinamicosRepository repo;

    @PostMapping("/hechos/crear")
    public ResponseEntity<HechosOutputDto> crearHecho(@RequestBody @Valid HechosInputDto input) {
        HechosOutputDto result = service.crearHecho(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/hechos/editar/{id}")
    public ResponseEntity<HechosOutputDto> editar(@PathVariable Long id, @RequestBody HechosOutputDto out) {
        return ResponseEntity.ok(service.editarHecho(id, out));
    }

    // extra: listar hechos desde BD
    @GetMapping("/hechos")
    public ResponseEntity<List<HechosOutputDto>> listar() {
        return ResponseEntity.ok(
                repo.findAll().stream().map(h -> service.getApiMapper().toOutput(h)).toList()
        );
    }
}

