package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.servicioAgregador.domain.Contribuyente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContribuyenteRepository extends JpaRepository<Contribuyente, Long> {
}