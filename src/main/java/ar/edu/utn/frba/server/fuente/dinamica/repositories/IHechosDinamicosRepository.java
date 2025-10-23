package ar.edu.utn.frba.server.fuente.dinamica.repositories;

import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IHechosDinamicosRepository extends JpaRepository<Hecho, Long> {

    List<Hecho> findByEliminadoFalse();

    List<Hecho> findByProvinciaAndEliminadoFalse(String provincia);

    List<Hecho> findByCategoriaAndEliminadoFalse(String categoria);


    // Trae de la tabla 'hecho' explícitamente
    @Query(value = "SELECT * FROM hecho", nativeQuery = true)
    List<Hecho> findAllFromHecho();
}
