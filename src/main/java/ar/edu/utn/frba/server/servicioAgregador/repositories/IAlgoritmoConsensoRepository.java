package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.servicioAgregador.dtos.AlgoritmoConsenso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAlgoritmoConsensoRepository extends JpaRepository<AlgoritmoConsenso, Long> {
}