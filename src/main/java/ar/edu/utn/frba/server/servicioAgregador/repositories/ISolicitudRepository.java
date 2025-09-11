package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudRepository {
    void guardarSolicitud(SolicitudEliminacion solicitud);
    List<SolicitudEliminacion> findAll();
}
