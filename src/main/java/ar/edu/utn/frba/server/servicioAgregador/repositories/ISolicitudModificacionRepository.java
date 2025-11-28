package ar.edu.utn.frba.server.servicioAgregador.repositories;


import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudModificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudModificacionRepository extends JpaRepository<SolicitudModificacion, Long> {
}
