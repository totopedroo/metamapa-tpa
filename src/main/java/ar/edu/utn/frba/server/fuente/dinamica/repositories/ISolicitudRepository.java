package ar.edu.utn.frba.server.fuente.dinamica.repositories;

import ar.edu.utn.frba.server.fuente.dinamica.domain.SolicitudEliminacion;

import java.util.List;
import java.util.Optional;

public interface ISolicitudRepository {
    SolicitudEliminacion save(SolicitudEliminacion solicitud);  // crea/actualiza
    Optional<SolicitudEliminacion> findById(Long id);
    List<SolicitudEliminacion> findByHecho(Long idHecho);
    List<SolicitudEliminacion> findAll();
}
