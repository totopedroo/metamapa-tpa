package ar.edu.utn.frba.server.servicioUsuarios.services;

import ar.edu.utn.frba.server.servicioUsuarios.exceptions.UsuarioYaExistenteException;
import ar.edu.utn.frba.server.servicioUsuarios.domain.Rol;
import ar.edu.utn.frba.server.servicioUsuarios.domain.Usuario;
import ar.edu.utn.frba.server.servicioUsuarios.dtos.UsuarioDtoInput;
import ar.edu.utn.frba.server.servicioUsuarios.repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService {
    @Autowired
    private UsuariosRepository repo; // O como se llame tu repo

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Usuario crearUsuario(UsuarioDtoInput req) {

        if (repo.findByNombreDeUsuario(req.getUsername()).isPresent()) {
            throw new UsuarioYaExistenteException("El nombre de usuario ya existe");
        }

        // 2. El mapeo también vive acá.
        Usuario u = new Usuario();
        u.setNombreDeUsuario(req.getUsername().trim());
        u.setContrasenia(encoder.encode(req.getPassword()));
        u.setRol(Rol.valueOf(req.getRol().trim().toUpperCase()));
        u.setHabilitado(true);
        u.setBloqueado(false);

        return repo.save(u);
    }
}
