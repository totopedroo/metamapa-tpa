package ar.edu.utn.frba.server.servicioUsuarios.services;

import ar.edu.utn.frba.server.servicioUsuarios.domain.Usuario;
import ar.edu.utn.frba.server.servicioUsuarios.repositories.UsuariosRepository;
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
        /*if (u.getPermisos() != null) {
            u.getPermisos().forEach(p -> {
                String name = (p instanceof Enum<?> e) ? e.name() : String.valueOf(p);
                if (!name.startsWith("PERM_")) name = "PERM_" + name;
                auths.add(new SimpleGrantedAuthority(name));
            });*/


        return new org.springframework.security.core.userdetails.User(
                u.getNombreDeUsuario(),
                u.getContrasenia(),             // hash BCrypt
                u.isHabilitado(),               // enabled
                true, true, !u.isBloqueado(),   // accountNonExpired, credentialsNonExpired, accountNonLocked
                auths
        );
    }
}