package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFuenteRepository extends JpaRepository<Fuente, Long> {

    Optional<Fuente> findByTipo(TipoFuente tipo);
}