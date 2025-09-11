package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosInputDto;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;

import java.time.LocalDate;
import java.util.List;

public interface IHechosService
{

    HechosOutputDto crearHecho(HechosInputDto inputDto);

    List<Hecho> buscar(String criterio);

    List<HechosOutputDto> filtrarHechos(String categoria, LocalDate fechaReporteDesde, LocalDate fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta, Double latitud, Double longitud);
}

