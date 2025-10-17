package ar.edu.utn.frba.server.gestorUsuarios.controllers;

import ar.edu.utn.frba.server.gestorUsuarios.domain.Rol;
import ar.edu.utn.frba.server.gestorUsuarios.dtos.UsuarioDtoInput;
import ar.edu.utn.frba.server.gestorUsuarios.dtos.UsuarioCreatedDTO;
import ar.edu.utn.frba.server.gestorUsuarios.domain.Usuario;
import ar.edu.utn.frba.server.gestorUsuarios.dtos.UsuarioDtoInput;
import ar.edu.utn.frba.server.gestorUsuarios.repository.UsuariosRepository;
import ar.edu.utn.frba.server.gestorUsuarios.services.UsuariosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private UsuariosService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioDtoInput req) {
        Usuario nuevoUsuario = usuarioService.crearUsuario(req);
        var body = new UsuarioCreatedDTO(nuevoUsuario.getId(), nuevoUsuario.getNombreDeUsuario(), nuevoUsuario.getRol());
        return ResponseEntity.created(URI.create("/usuarios/" + nuevoUsuario.getId())).body(body);
    }
}