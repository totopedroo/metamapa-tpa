package ar.edu.utn.frba.Server.Servicio_Agregador.Service;

import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosInputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;

import java.time.LocalDate;
import java.util.List;

public interface IHechosService
{

    public HechosOutputDto crearHecho(HechosInputDto inputDto);

    HechosOutputDto convertirDto(Hecho hechos);
    public void  setConsensuado(Hecho hecho);
    List<HechosOutputDto> filtrarHechos(String categoria, LocalDate fechaReporteDesde, LocalDate fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta, Double latitud, Double longitud);
}

