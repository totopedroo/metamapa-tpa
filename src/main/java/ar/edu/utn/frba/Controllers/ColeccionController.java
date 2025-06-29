package ar.edu.utn.frba.Controllers;

import ar.edu.utn.frba.Servicio_Agregador.Dtos.ColeccionInputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.HechosOutputDto;

import ar.edu.utn.frba.Servicio_Agregador.Repository.ColeccionRepository;
import ar.edu.utn.frba.Servicio_Agregador.Service.IColeccionService;
import ar.edu.utn.frba.Servicio_Agregador.Service.ISeederService;

import ar.edu.utn.frba.Servicio_Agregador.Service.HechosService;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.UUID;
import java.security.SecureRandom;
@RestController
@RequestMapping
@CrossOrigin("http://localhost:8080")
public class ColeccionController {
    private final SecureRandom secureRandom = new SecureRandom();
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

    @GetMapping("/createColeccionAPI")
    public Coleccion crearColeccionPruebaAPI() {
        return coleccionService.setColeccionApi();
    }
/*
    @GetMapping("/createColeccionCSV")
    public Coleccion crearColeccionPruebaCSV() {
        return coleccionService.setColeccionCsv();
    }
*/
    @PostMapping("/colecciones/{coleccionId}/hechos/{hechoId}")
    public ResponseEntity<?> agregarHechoAColeccion(@PathVariable String coleccionId, @PathVariable Long hechoId) {
        try {
            ColeccionOutputDto coleccionActualizada = coleccionService.agregarHechoAColeccion(coleccionId, hechoId);
            return ResponseEntity.ok(coleccionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    }




