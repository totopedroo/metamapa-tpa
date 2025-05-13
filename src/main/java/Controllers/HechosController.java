package Controllers;

import Dtos.HechosOutputDto;
import Service.IHechosService;

import java.util.List;

public class HechosController {
public IHechosService hechosService;
    List<HechosOutputDto> buscarTodosLosHechos(){
        return hechosService.buscarTodos();

    }
}
