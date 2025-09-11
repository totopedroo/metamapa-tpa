package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.fuente.dinamica.dtos.SolicitudInputDto;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.SolicitudOutputDto;

public interface ISolicitudService {
    SolicitudOutputDto crearSolicitud(SolicitudInputDto inputDto);
    void aceptarSolicitud(long idSolicitud);
    void rechazarSolicitud(long idSolicitud);

}

