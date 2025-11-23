package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
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
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin("http://localhost:8082")
public class HechosController {

    @Autowired
    private IHechosService hechosService;

    @PostMapping(path = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    // POST /hechos/importar-api  → trae de la API y guarda en la DB
    @PostMapping("/importar-api")
    public ResponseEntity<?> importarApi() {
        List<Hecho> guardados = hechosService.importarDesdeApi();
        return ResponseEntity.ok(Map.of(
                "insertados", guardados.size()
        ));
    }

    // GET /hechos → devuelve todo lo que hay en la tabla
  /*  @GetMapping
    public List<Hecho> listar() {
        return hechosRepository.findAll();
    }*/
}
/*
    @GetMapping("/inicializar")
    public Boolean inicializar() {
        this.seederService.inicializar();
        return true;
    }*/

