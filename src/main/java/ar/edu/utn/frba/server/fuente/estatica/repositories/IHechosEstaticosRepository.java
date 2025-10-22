package ar.edu.utn.frba.server.fuente.estatica.repositories;

import ar.edu.utn.frba.server.fuente.estatica.domain.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IHechosEstaticosRepository extends JpaRepository<Hecho, Long> {

    // Usados por tu ExportacionCSVService
    List<Hecho> findByEliminadoFalse();

    List<Hecho> findByProvinciaAndEliminadoFalse(String provincia);

    List<Hecho> findByCategoriaAndEliminadoFalse(String categoria);

    @Query("SELECT h.provincia, COUNT(h) FROM hecho_est h WHERE h.eliminado = false GROUP BY h.provincia")
    List<Object[]> contarHechosPorProvincia();

    @Query("SELECT h.categoria, COUNT(h) FROM hecho_est h WHERE h.eliminado = false GROUP BY h.categoria")
    List<Object[]> contarHechosPorCategoria();

    // Si seguís necesitando “no sincronizados”, podés derivarlo de algún flag que tengas
    @Query("SELECT h FROM hecho_est h WHERE h.eliminado = false") // ejemplo neutral
    List<Hecho> buscarNoSincronizados();

    // Trae de la tabla 'hecho' explícitamente
    @Query(value = "SELECT * FROM hecho", nativeQuery = true)
    List<Hecho> findAllFromHecho();
}
