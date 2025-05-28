package ar.edu.utn.frba.Service;

import ar.edu.utn.frba.Dtos.SolicitudOutputDto;
import ar.edu.utn.frba.Dtos.SolicitudInputDto;

public interface ISolicitudService {
    SolicitudOutputDto crearSolicitud(SolicitudInputDto inputDto);
    void aceptarSolicitud(long idSolicitud);
    void rechazarSolicitud(long idSolicitud);

}

