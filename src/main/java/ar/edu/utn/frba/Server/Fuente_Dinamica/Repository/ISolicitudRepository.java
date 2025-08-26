package ar.edu.utn.frba.Server.Fuente_Dinamica.Repository;

import ar.edu.utn.frba.Server.Fuente_Dinamica.Domain.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudRepository {
    void guardarSolicitud(SolicitudEliminacion solicitud);
    List<SolicitudEliminacion> findAll();
}
