package ar.edu.utn.frba.Servicio_Agregador.Repository;

import ar.edu.utn.frba.Servicio_Agregador.Domain.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudRepository {
    void guardarSolicitud(SolicitudEliminacion solicitud);
    List<SolicitudEliminacion> findAll();
}
