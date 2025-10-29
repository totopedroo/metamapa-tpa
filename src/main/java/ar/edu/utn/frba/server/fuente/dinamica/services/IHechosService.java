package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;

import java.time.LocalDate;
import java.util.List;

public interface IHechosService
{

    List<Hecho> buscar(String criterio);

    List<HechosOutputDto> filtrarHechos(String categoria, LocalDate fechaReporteDesde, LocalDate fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta, Double latitud, Double longitud);
}

