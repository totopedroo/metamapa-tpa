package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.servicioAgregador.dtos.AlgoritmoConsenso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAlgoritmoConsensoRepository extends JpaRepository<AlgoritmoConsenso, Long> {

    Optional<AlgoritmoConsenso> findByNombre(String nombre);
}