package ar.edu.utn.frba.server.gestorUsuarios.controllers;

import ar.edu.utn.frba.server.gestorUsuarios.domain.Rol;
import ar.edu.utn.frba.server.gestorUsuarios.dtos.UsuarioDtoInput;
import ar.edu.utn.frba.server.gestorUsuarios.dtos.UsuarioCreatedDTO;
import ar.edu.utn.frba.server.gestorUsuarios.domain.Usuario;
import ar.edu.utn.frba.server.gestorUsuarios.dtos.UsuarioDtoInput;
import ar.edu.utn.frba.server.gestorUsuarios.repository.UsuariosRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    private final UsuariosRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuariosController(UsuariosRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/register")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDtoInput req) {

        if (req.getUsername() == null || req.getUsername().isBlank()
                || req.getPassword() == null || req.getPassword().isBlank()
                || req.getRol() == null || req.getRol().isBlank()) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", "username, password y rol son obligatorios"));
        }


        if (repo.findByNombreDeUsuario(req.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(java.util.Map.of("error", "El nombre de usuario ya existe"));
        }

        // Mapear DTO -> entidad
        Usuario u = new Usuario();
        u.setNombreDeUsuario(req.getUsername().trim());
        u.setContrasenia(encoder.encode(req.getPassword())); // IMPORTANT: hash
        u.setRol(Rol.valueOf(req.getRol().trim().toUpperCase()));         // ADMIN/CONTRIBUYENTE/VISUALIZADOR
        u.setHabilitado(true);
        u.setBloqueado(false);

        // Persistir
        Usuario saved = repo.save(u);

        var body = new UsuarioCreatedDTO(saved.getId(), saved.getNombreDeUsuario(), saved.getRol());
        return ResponseEntity.created(URI.create("/usuarios/" + saved.getId())).body(body);
    }
}