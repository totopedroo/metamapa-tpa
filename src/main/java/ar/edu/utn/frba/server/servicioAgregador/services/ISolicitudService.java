package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudOutputDto;

public interface ISolicitudService {
    SolicitudOutputDto crearSolicitud(SolicitudInputDto inputDto);
    void aceptarSolicitud(long idSolicitud);
    void rechazarSolicitud(long idSolicitud);

}

