package ar.edu.utn.frba.Server.Fuente_Proxy.Service;

import ar.edu.utn.frba.Server.Fuente_Proxy.Dtos.SolicitudInputDto;
import ar.edu.utn.frba.Server.Fuente_Proxy.Dtos.SolicitudOutputDto;

public interface ISolicitudService {
    SolicitudOutputDto crearSolicitud(SolicitudInputDto inputDto);
    void aceptarSolicitud(long idSolicitud);
    void rechazarSolicitud(long idSolicitud);

}

