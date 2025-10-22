package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping
@CrossOrigin("http://localhost:8080")
public class HechosController {

    @Autowired
    private IHechosService hechosService;

    @PostMapping(path = "/hechos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HechosOutputDto crearHecho(@RequestBody HechosInputDto inputDto) {
        return hechosService.crearHecho(inputDto);
    }

    @GetMapping("/hechos")
    public List<HechosOutputDto> filtrarHechos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteHasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoHasta,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud) {
        return hechosService.filtrarHechos(categoria, fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }

    @GetMapping("/hechos/{id}")
    public HechosOutputDto obtenerHecho(@PathVariable Long id) {
        return hechosService.obtenerHecho(id);
    }

    @PatchMapping("/hechos/{id}")
    public ResponseEntity<HechosOutputDto> editarHecho(
            @PathVariable Long id,
            @RequestBody HechosInputDto dto) {

        HechosOutputDto edited = hechosService.editarHecho(id, dto);
        return ResponseEntity.ok(edited);
    }
}
