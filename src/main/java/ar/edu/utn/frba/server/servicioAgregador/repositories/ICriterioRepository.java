package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.servicioAgregador.domain.CriterioDePertenencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICriterioRepository extends JpaRepository<CriterioDePertenencia, Long> {}
