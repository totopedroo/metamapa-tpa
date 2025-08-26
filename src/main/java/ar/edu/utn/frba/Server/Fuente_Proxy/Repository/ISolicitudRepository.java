package ar.edu.utn.frba.Server.Fuente_Proxy.Repository;

import ar.edu.utn.frba.Server.Fuente_Proxy.Domain.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudRepository {
    void guardarSolicitud(SolicitudEliminacion solicitud);
    List<SolicitudEliminacion> findAll();
}
