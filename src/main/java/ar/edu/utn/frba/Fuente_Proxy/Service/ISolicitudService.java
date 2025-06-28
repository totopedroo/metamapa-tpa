package ar.edu.utn.frba.Fuente_Proxy.Service;

import ar.edu.utn.frba.Dtos.SolicitudInputDto;
import ar.edu.utn.frba.Dtos.SolicitudOutputDto;

public interface ISolicitudService {
    SolicitudOutputDto crearSolicitud(SolicitudInputDto inputDto);
    void aceptarSolicitud(long idSolicitud);
    void rechazarSolicitud(long idSolicitud);

}

