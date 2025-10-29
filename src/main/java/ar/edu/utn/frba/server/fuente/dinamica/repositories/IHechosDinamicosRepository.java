package ar.edu.utn.frba.server.fuente.dinamica.repositories;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IHechosDinamicosRepository extends JpaRepository<Hecho, Long> {

    List<Hecho> findByEliminadoFalse();

    List<Hecho> findByProvinciaAndEliminadoFalse(String provincia);

    List<Hecho> findByCategoriaAndEliminadoFalse(String categoria);
    Optional<Hecho> findByTituloAndFechaAcontecimiento(String titulo, LocalDate fechaAcontecimiento);


    // Trae de la tabla 'hecho' explícitamente
    @Query(value = "SELECT * FROM hecho", nativeQuery = true)
    List<Hecho> findAllFromHecho();
}
