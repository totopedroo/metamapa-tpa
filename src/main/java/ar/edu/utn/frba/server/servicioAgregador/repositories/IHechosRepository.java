package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.common.enums.EstadoRevisionHecho;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {

    // Consultas para estadísticas
    @Query("""
SELECT 
  COALESCE(NULLIF(TRIM(h.provincia), ''), 'Desconocida') AS provincia,
  COUNT(h)
FROM hecho_sa h
WHERE h.eliminado = false
GROUP BY COALESCE(NULLIF(TRIM(h.provincia), ''), 'Desconocida')
ORDER BY COUNT(h) DESC
""")
    List<Object[]> contarHechosPorProvincia();

    @Query("""
SELECT 
  COALESCE(NULLIF(TRIM(h.categoria), ''), 'Sin categoría') AS categoria,
  COUNT(h)
FROM hecho_sa h
WHERE h.eliminado = false
GROUP BY COALESCE(NULLIF(TRIM(h.categoria), ''), 'Sin categoría')
ORDER BY COUNT(h) DESC
""")
    List<Object[]> contarHechosPorCategoria();

    @Query("""
SELECT 
  COALESCE(NULLIF(TRIM(h.provincia), ''), 'Desconocida') AS provincia,
  COUNT(h)
FROM hecho_sa h
WHERE h.eliminado = false AND LOWER(h.categoria) = LOWER(:categoria)
GROUP BY COALESCE(NULLIF(TRIM(h.provincia), ''), 'Desconocida')
ORDER BY COUNT(h) DESC
""")
    List<Object[]> contarHechosPorProvinciaYCategoria(@Param("categoria") String categoria);

    @Query("SELECT h.horaAcontecimiento, COUNT(h) FROM hecho_sa h WHERE h.eliminado = false AND h.categoria = :categoria AND h.horaAcontecimiento IS NOT NULL GROUP BY h.horaAcontecimiento ORDER BY COUNT(h) DESC")
    List<Object[]> contarHechosPorHoraYCategoria(@Param("categoria") String categoria);

    // Consultas para búsqueda
    @Query("SELECT h FROM hecho_sa h WHERE h.eliminado = false AND " +
            "(:titulo IS NULL OR LOWER(h.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
            "(:categoria IS NULL OR LOWER(h.categoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) AND " +
            "(:provincia IS NULL OR LOWER(h.provincia) LIKE LOWER(CONCAT('%', :provincia, '%')))")
    List<Hecho> buscarHechos(@Param("titulo") String titulo,
                             @Param("categoria") String categoria,
                             @Param("provincia") String provincia);

    // Consulta para hechos por colección
    @Query("SELECT h FROM hecho_sa h JOIN h.colecciones c WHERE c.id = :coleccionId AND h.eliminado = false")
    List<Hecho> findByColeccionId(@Param("coleccionId") Long coleccionId);

    // Consulta para hechos no eliminados
    List<Hecho> findByEliminadoFalse();

    // Consulta para hechos por provincia
    List<Hecho> findByProvinciaAndEliminadoFalse(String provincia);

    // Consulta para hechos por categoría
    List<Hecho> findByCategoriaAndEliminadoFalse(String categoria);

    @Query("SELECT COUNT(h) FROM hecho_sa h WHERE h.eliminado = false")
    long countActivos();

    List<Hecho> findByEstadoRevision(EstadoRevisionHecho estadoRevision);

    @Query("SELECT COUNT(h) FROM hecho_sa h WHERE h.eliminado = false AND h.estadoRevision = 'PENDIENTE'")
    long countPendientes();

    boolean existsByTituloAndFechaAcontecimientoAndLatitudAndLongitud(
            String titulo, LocalDate fecha, Double lat, Double lon);
}