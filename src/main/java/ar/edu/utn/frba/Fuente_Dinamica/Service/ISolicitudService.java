package ar.edu.utn.frba.Fuente_Dinamica.Service;

import ar.edu.utn.frba.Fuente_Dinamica.Dtos.SolicitudInputDto;
import ar.edu.utn.frba.Fuente_Dinamica.Dtos.SolicitudOutputDto;

public interface ISolicitudService {
    SolicitudOutputDto crearSolicitud(SolicitudInputDto inputDto);
    void aceptarSolicitud(long idSolicitud);
    void rechazarSolicitud(long idSolicitud);

}

