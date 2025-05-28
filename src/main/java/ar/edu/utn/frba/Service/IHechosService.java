package ar.edu.utn.frba.Service;

import ar.edu.utn.frba.Dtos.HechosInputDto;
import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.domain.Contribuyente;


import java.util.List;

public interface IHechosService
{

    public List<HechosOutputDto> buscarTodos();
    public HechosOutputDto crearHecho(Contribuyente contribuyente, HechosInputDto inputDto);
}

