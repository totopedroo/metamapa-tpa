package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechoDTO; // DTO Simple

import java.time.LocalDate;
import java.util.List;

public interface IHechosService
{

    HechosOutputDto crearHecho(HechosInputDto inputDto);
    HechosOutputDto obtenerHecho(Long id);
    HechosOutputDto convertirDto(Hecho hechos);
    void  setConsensuado(Hecho hecho, EstadoConsenso estado);
    List<Hecho> importarDesdeApi() ;
    List<HechosOutputDto> filtrarHechos(String categoria, LocalDate fechaReporteDesde, LocalDate fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta, Double latitud, Double longitud);
    List<HechosOutputDto> listarHechosPorUsuario(Long idUsuario);
    List<HechoDTO> obtenerHechosLanding(String modo, int limit);
}

