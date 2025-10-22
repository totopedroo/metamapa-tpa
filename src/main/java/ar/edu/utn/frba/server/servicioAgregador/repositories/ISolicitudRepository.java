package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.common.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudRepository extends JpaRepository<SolicitudEliminacion, Long> {

    // Buscar solicitudes por estado
    List<SolicitudEliminacion> findByEstado(EstadoDeSolicitud estado);

    // Buscar solicitudes por hecho
    List<SolicitudEliminacion> findByIdHechoAsociado(Long idHecho);

    // Contar solicitudes spam (asumiendo que hay un campo para detectar spam)
    @Query("SELECT COUNT(s) FROM SolicitudEliminacion s WHERE s.esSpam = true")
    Long contarSolicitudesSpam();

    // Contar solicitudes por estado
    @Query("SELECT s.estado, COUNT(s) FROM SolicitudEliminacion s GROUP BY s.estado")
    List<Object[]> contarSolicitudesPorEstado();

    @Query("SELECT COUNT(s) FROM SolicitudEliminacion s WHERE s.estado = 'PENDIENTE'")
    long countPendientes();
}
