package ar.edu.utn.frba.Server.Servicio_Agregador.Controllers;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.BusquedaTextoLibreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buscar")
@CrossOrigin("http://localhost:8080")
public class BusquedaTextoLibreController {

    @Autowired
    private BusquedaTextoLibreService busquedaService;

    /**
     * Búsqueda por texto libre
     */
    @GetMapping("/texto-libre")
    public ResponseEntity<List<HechosOutputDto>> buscarPorTextoLibre(
            @RequestParam String texto) {
        List<Hecho> hechos = busquedaService.buscarPorTextoLibre(texto);
        List<HechosOutputDto> resultados = hechos.stream()
                .map(HechosOutputDto::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultados);
    }

    /**
     * Búsqueda avanzada con múltiples criterios
     */
    @GetMapping("/avanzada")
    public ResponseEntity<List<HechosOutputDto>> busquedaAvanzada(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String provincia) {
        List<Hecho> hechos = busquedaService.busquedaAvanzada(texto, categoria, provincia);
        List<HechosOutputDto> resultados = hechos.stream()
                .map(HechosOutputDto::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultados);
    }

    /**
     * Búsqueda por palabras clave
     */
    @GetMapping("/palabras-clave")
    public ResponseEntity<List<HechosOutputDto>> buscarPorPalabrasClave(
            @RequestParam String texto) {
        List<Hecho> hechos = busquedaService.buscarPorPalabrasClave(texto);
        List<HechosOutputDto> resultados = hechos.stream()
                .map(HechosOutputDto::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultados);
    }
}
