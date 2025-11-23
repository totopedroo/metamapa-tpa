package ar.edu.utn.frba.server.gestorUsuarios.services;

import ar.edu.utn.frba.server.gestorUsuarios.domain.Usuario;
import ar.edu.utn.frba.server.gestorUsuarios.repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuariosRepository usuariosRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuariosRepository.findByNombreDeUsuario(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<GrantedAuthority> auths = new ArrayList<>();
        if (u.getRol() != null) {
            auths.add(new SimpleGrantedAuthority("ROLE_" + u.getRol().name()));
        }

        // Si decidiste usar List<String> en permisos, puedes descomentar esto:
        /*
        if (u.getPermisos() != null) {
            u.getPermisos().forEach(permiso -> {
                String authName = permiso;
                if (!authName.startsWith("PERM_")) authName = "PERM_" + authName;
                auths.add(new SimpleGrantedAuthority(authName));
            });
        }
        */

        // CORRECCIÓN AQUÍ:
        // 1. Usamos getHabilitado() en lugar de isHabilitado()
        // 2. Usamos Boolean.TRUE.equals(...) para manejar nulos de forma segura
        boolean enabled = Boolean.TRUE.equals(u.getHabilitado());

        // accountNonLocked es true si el usuario NO está bloqueado
        boolean accountNonLocked = !Boolean.TRUE.equals(u.getBloqueado());

        return new org.springframework.security.core.userdetails.User(
            u.getNombreDeUsuario(),
            u.getContrasenia(),             // hash BCrypt
            enabled,                        // enabled
            true,                           // accountNonExpired
            true,                           // credentialsNonExpired
            accountNonLocked,               // accountNonLocked
            auths
        );
    }
}