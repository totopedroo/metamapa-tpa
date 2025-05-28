package ar.edu.utn.frba.Service;

import ar.edu.utn.frba.Dtos.HechosInputDto;
import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.domain.Contribuyente;
import ar.edu.utn.frba.domain.Hecho;


import java.time.LocalDate;
import java.util.List;

public interface IHechosService
{

    public HechosOutputDto crearHecho(Contribuyente contribuyente, HechosInputDto inputDto);

    HechosOutputDto convertirDto(Hecho hechos);

    List<HechosOutputDto> filtrarHechos(String categoria, LocalDate fechaReporteDesde, LocalDate fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta, Double latitud, Double longitud);
}

