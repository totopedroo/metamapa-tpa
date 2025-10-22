package ar.edu.utn.frba.server.servicioUsuarios.controllers;

import ar.edu.utn.frba.server.servicioUsuarios.dtos.UsuarioDtoInput;
import ar.edu.utn.frba.server.servicioUsuarios.dtos.UsuarioCreatedDTO;
import ar.edu.utn.frba.server.servicioUsuarios.domain.Usuario;
import ar.edu.utn.frba.server.servicioUsuarios.services.UsuariosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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