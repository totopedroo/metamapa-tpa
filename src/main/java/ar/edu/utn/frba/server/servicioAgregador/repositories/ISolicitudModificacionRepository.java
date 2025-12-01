package ar.edu.utn.frba.server.servicioAgregador.repositories;


import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudModificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ISolicitudModificacionRepository extends JpaRepository<SolicitudModificacion, Long> {

    List<SolicitudModificacion> findByEstado(EstadoDeSolicitud estado);
}
