package ar.edu.utn.frba.Server.Servicio_Agregador.Controllers;

import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosInputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.HechosRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.IHechosService;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ISeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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

    @PostMapping(
            path = "/crear",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void crearHecho(@RequestBody HechosInputDto inputDto) {
        hechosService.crearHecho(inputDto);
    }
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

