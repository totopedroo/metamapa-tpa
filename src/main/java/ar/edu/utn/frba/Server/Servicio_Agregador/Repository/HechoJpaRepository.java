package ar.edu.utn.frba.Server.Servicio_Agregador.Repository;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface HechoJpaRepository extends JpaRepository<Hecho, Long> {

        // Consultas para estadísticas
        @Query("SELECT h.provincia, COUNT(h) FROM Hecho h WHERE h.eliminado = false GROUP BY h.provincia ORDER BY COUNT(h) DESC")
        List<Object[]> contarHechosPorProvincia();

        @Query("SELECT h.categoria, COUNT(h) FROM Hecho h WHERE h.eliminado = false GROUP BY h.categoria ORDER BY COUNT(h) DESC")
        List<Object[]> contarHechosPorCategoria();

        @Query("SELECT h.provincia, COUNT(h) FROM Hecho h WHERE h.eliminado = false AND h.categoria = :categoria GROUP BY h.provincia ORDER BY COUNT(h) DESC")
        List<Object[]> contarHechosPorProvinciaYCategoria(@Param("categoria") String categoria);

        @Query("SELECT h.horaAcontecimiento, COUNT(h) FROM Hecho h WHERE h.eliminado = false AND h.categoria = :categoria AND h.horaAcontecimiento IS NOT NULL GROUP BY h.horaAcontecimiento ORDER BY COUNT(h) DESC")
        List<Object[]> contarHechosPorHoraYCategoria(@Param("categoria") String categoria);

        // Consultas para búsqueda
        @Query("SELECT h FROM Hecho h WHERE h.eliminado = false AND " +
                        "(:titulo IS NULL OR LOWER(h.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
                        "(:categoria IS NULL OR LOWER(h.categoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) AND " +
                        "(:provincia IS NULL OR LOWER(h.provincia) LIKE LOWER(CONCAT('%', :provincia, '%')))")
        List<Hecho> buscarHechos(@Param("titulo") String titulo,
                        @Param("categoria") String categoria,
                        @Param("provincia") String provincia);

        // Consulta para hechos por colección
        @Query("SELECT h FROM Hecho h JOIN h.colecciones c WHERE c.id = :coleccionId AND h.eliminado = false")
        List<Hecho> findByColeccionId(@Param("coleccionId") Long coleccionId);

        // Consulta para hechos no eliminados
        List<Hecho> findByEliminadoFalse();

        // Consulta para hechos por provincia
        List<Hecho> findByProvinciaAndEliminadoFalse(String provincia);

        // Consulta para hechos por categoría
        List<Hecho> findByCategoriaAndEliminadoFalse(String categoria);
}
