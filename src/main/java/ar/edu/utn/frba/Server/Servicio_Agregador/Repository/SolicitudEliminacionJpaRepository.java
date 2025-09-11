package ar.edu.utn.frba.Server.Servicio_Agregador.Repository;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.SolicitudEliminacion;
import ar.edu.utn.frba.Server.Enums.EstadoDeSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudEliminacionJpaRepository extends JpaRepository<SolicitudEliminacion, Long> {

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
}
