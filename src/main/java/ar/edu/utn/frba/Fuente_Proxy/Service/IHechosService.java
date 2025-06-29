package ar.edu.utn.frba.Fuente_Proxy.Service;

import ar.edu.utn.frba.Fuente_Proxy.Dtos.HechosInputDto;
import ar.edu.utn.frba.Fuente_Proxy.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Fuente_Proxy.Domain.Hecho;

import java.time.LocalDate;
import java.util.List;

public interface IHechosService
{

    public HechosOutputDto crearHecho(HechosInputDto inputDto);

    HechosOutputDto convertirDto(Hecho hechos);

    List<HechosOutputDto> filtrarHechos(String categoria, LocalDate fechaReporteDesde, LocalDate fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta, Double latitud, Double longitud);
}

