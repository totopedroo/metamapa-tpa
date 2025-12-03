package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudEliminacion;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudFrontDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudOutputDto;

import java.util.List;

public interface ISolicitudService {
    SolicitudOutputDto crearSolicitud(SolicitudInputDto inputDto);
    void aceptarSolicitud(long idSolicitud);
    void rechazarSolicitud(long idSolicitud);
    List<SolicitudFrontDto> obtenerSolicitudesConTitulo();
    List<SolicitudFrontDto> obtenerPorUsuario(Long idUsuario);
}

