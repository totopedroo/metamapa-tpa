package ar.edu.utn.frba.Controllers;

import ar.edu.utn.frba.Dtos.ColeccionInputDto;
import ar.edu.utn.frba.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Dtos.HechosOutputDto;

import ar.edu.utn.frba.Repository.Implementacion.ColeccionRepository;
import ar.edu.utn.frba.Service.IColeccionService;
import ar.edu.utn.frba.Service.ISeederService;

import ar.edu.utn.frba.domain.Coleccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping
@CrossOrigin("http://localhost:8080")
public class ColeccionController {

    @Autowired
    private IColeccionService coleccionService;
    @Autowired
    private ISeederService seederService;
    @Autowired
    private ColeccionRepository coleccionRepository;

    @GetMapping("/colecciones")
    public List<ColeccionOutputDto> getColecciones() {

        return coleccionService.buscarTodos();

    }

    @GetMapping("/{id}/hechos")
    public List<HechosOutputDto> obtenerHechosDeColeccion(@PathVariable String id) {

        return coleccionService.obtenerHechosPorColeccion(id);
    }


    @GetMapping("/createPrueba")
    public Coleccion crearColeccionPrueba() {
        return coleccionService.setColeccionApi();
    }

}