package ar.edu.utn.frba.Controllers;

import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Service.IHechosService;
import ar.edu.utn.frba.Service.ISeederService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ISeederService seederService;
    @GetMapping("/hechos")
    List<HechosOutputDto> buscarTodosLosHechos(){
            return hechosService.buscarTodos();
    }

    @GetMapping("/inicializar")
    public Boolean inicializar() {
        this.seederService.inicializar();
        return true;
    }
    @GetMapping("/prueba")
    public String prueba() {
        return "Prueba exitosa";
    }
}
