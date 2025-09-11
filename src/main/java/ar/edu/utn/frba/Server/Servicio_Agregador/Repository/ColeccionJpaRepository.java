package ar.edu.utn.frba.Server.Servicio_Agregador.Repository;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColeccionJpaRepository extends JpaRepository<Coleccion, Long> {

    // Buscar colección por título
    Optional<Coleccion> findByTitulo(String titulo);

    // Buscar colecciones por administrador
    @Query("SELECT c FROM Coleccion c WHERE c.administrador.id = :administradorId")
    List<Coleccion> findByAdministradorId(@Param("administradorId") Long administradorId);

    // Contar hechos por colección
    @Query("SELECT c.id, c.titulo, COUNT(h) FROM Coleccion c LEFT JOIN c.hechos h WHERE h.eliminado = false OR h IS NULL GROUP BY c.id, c.titulo")
    List<Object[]> contarHechosPorColeccion();
}
