package ar.edu.utn.frba.Server.Fuente_Dinamica.Service;

import ar.edu.utn.frba.Server.Fuente_Dinamica.Dtos.HechosInputDto;
import ar.edu.utn.frba.Server.Fuente_Dinamica.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Server.Fuente_Dinamica.Domain.Hecho;

import java.time.LocalDate;
import java.util.List;

public interface IHechosService
{

    public HechosOutputDto crearHecho(HechosInputDto inputDto);

    HechosOutputDto convertirDto(Hecho hechos);

    List<HechosOutputDto> filtrarHechos(String categoria, LocalDate fechaReporteDesde, LocalDate fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta, Double latitud, Double longitud);
}

