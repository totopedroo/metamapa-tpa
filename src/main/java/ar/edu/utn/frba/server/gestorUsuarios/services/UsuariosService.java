package ar.edu.utn.frba.server.gestorUsuarios.services;

import ar.edu.utn.frba.server.gestorUsuarios.domain.Rol;
import ar.edu.utn.frba.server.gestorUsuarios.domain.Usuario;
import ar.edu.utn.frba.server.gestorUsuarios.dtos.UsuarioDtoInput;
import ar.edu.utn.frba.server.gestorUsuarios.repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario crearUsuario(UsuarioDtoInput req) {
        Usuario usuario = new Usuario();

        // Mapeo de datos
        usuario.setNombre(req.getNombre());
        // Concatenamos nombre y apellido para el campo 'nombre' de la BD si quieres, o guardamos apellido aparte si la entidad lo tiene.
        // Si la entidad Usuario tiene campo apellido: usuario.setApellido(req.getApellido());

        usuario.setMail(req.getMail());

        // IMPORTANTE: Usamos el mail como nombre_de_usuario para el login, o generamos uno.
        usuario.setNombreDeUsuario(req.getMail());

        usuario.setContrasenia(passwordEncoder.encode(req.getPassword()));

        // ROL POR DEFECTO
        usuario.setRol(Rol.CONTRIBUYENTE); // O el rol que uses por defecto (ej: USER)
        usuario.setHabilitado(true);
        usuario.setBloqueado(false);

        return usuariosRepository.save(usuario);
    }
}