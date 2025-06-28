package ar.edu.utn.frba.Fuente_Dinamica.Repository;

import ar.edu.utn.frba.domain.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudRepository {
    void guardarSolicitud(SolicitudEliminacion solicitud);
    List<SolicitudEliminacion> findAll();
}
