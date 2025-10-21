package ar.edu.utn.frba.server.gestorUsuarios.services;

import ar.edu.utn.frba.server.gestorUsuarios.dtos.UserRolesPermissionsDTO;
import ar.edu.utn.frba.server.config.NotFoundException;
import ar.edu.utn.frba.server.gestorUsuarios.domain.Usuario;
import ar.edu.utn.frba.server.gestorUsuarios.repository.UsuariosRepository;
import ar.edu.utn.frba.server.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LoginService {

    private final UsuariosRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;  // inyectamos JwtUtil

    public LoginService(UsuariosRepository usuariosRepository, JwtUtil jwtUtil) {
        this.usuariosRepository = usuariosRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario autenticarUsuario(String username, String password) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByNombreDeUsuario(username);
        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }

        Usuario usuario = usuarioOpt.get();
        if (!passwordEncoder.matches(password, usuario.getContrasenia())) {
            throw new NotFoundException("Usuario", username);
        }
        return usuario;
    }

    public String generarAccessToken(String username) {
        return jwtUtil.generarAccessToken(username);   // instancia, no estático
    }

    public String generarRefreshToken(String username) {
        return jwtUtil.generarRefreshToken(username);  // instancia, no estático
    }

    public UserRolesPermissionsDTO obtenerRolesYPermisosUsuario(String username) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByNombreDeUsuario(username);
        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }
        Usuario usuario = usuarioOpt.get();
        return UserRolesPermissionsDTO.builder()
                .username(usuario.getNombreDeUsuario())
                .rol(usuario.getRol())
                .permisos(usuario.getPermisos())
                .build();
    }
}
