package ar.edu.utn.frba.server.gestorUsuarios.repository; //

import ar.edu.utn.frba.server.gestorUsuarios.domain.Usuario; // <-- ajustá el import al paquete real de tu entidad
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsuariosRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombreDeUsuario(String nombreDeUsuario);
    boolean existsByNombreDeUsuario(String nombreDeUsuario);
}