package ar.edu.utn.frba.Controllers;

import ar.edu.utn.frba.Dtos.HechosInputDto;
import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.Repository.Implementacion.HechosRepository;
import ar.edu.utn.frba.Service.IHechosService;
import ar.edu.utn.frba.Service.ISeederService;
import ar.edu.utn.frba.domain.Contribuyente;
import ar.edu.utn.frba.domain.FuenteDinamica;
import ar.edu.utn.frba.domain.FuenteDinamicaImpl;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping
@CrossOrigin("http://localhost:8080")
public class HechosController {

    @Autowired
    private IHechosService hechosService;
    @Autowired
    private HechosRepository hechosRepository;
    @Autowired
    private ISeederService seederService;


    @GetMapping("/hechos")
    public List<HechosOutputDto> filtrarHechos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaReporteHasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaAcontecimientoHasta,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud
    ) {
        return hechosService.filtrarHechos(categoria, fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }
    @GetMapping("/inicializar")
    public Boolean inicializar() {
        this.seederService.inicializar();
        return true;
    }



}

