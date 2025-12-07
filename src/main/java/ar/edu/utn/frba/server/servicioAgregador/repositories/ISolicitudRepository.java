package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT s FROM SolicitudEliminacion s ORDER BY " +
            "CASE WHEN s.estado = 'PENDIENTE' THEN 1 " +
            "WHEN s.estado = 'ACEPTADA' THEN 2 " +
            "WHEN s.estado = 'RECHAZADA' THEN 3 " +
            "ELSE 4 END")
    List<SolicitudEliminacion> findAllOrdenadasPorPrioridad();
}
