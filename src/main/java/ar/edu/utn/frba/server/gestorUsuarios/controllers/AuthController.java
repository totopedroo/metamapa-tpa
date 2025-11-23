package ar.edu.utn.frba.server.gestorUsuarios.controllers;

import ar.edu.utn.frba.server.config.NotFoundException;
import ar.edu.utn.frba.server.gestorUsuarios.dtos.AuthResponseDTO;
import ar.edu.utn.frba.server.gestorUsuarios.services.LoginService;
import ar.edu.utn.frba.server.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    @PostMapping(value="/login", consumes="application/json", produces="application/json")
    public ResponseEntity<?> loginApi(@RequestBody Map<String,String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        System.out.println(">>> BACKEND: Intento de login para: " + username);

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username y password son obligatorios"));
        }

        try {
            // 1. Autenticamos
            var usuario = loginService.autenticarUsuario(username, password);
            System.out.println(">>> BACKEND: Usuario autenticado: " + usuario.getId());

            // 2. Generamos Tokens (OJO: Aquí suele fallar si JwtUtil no está bien configurado)
            String accessToken = jwtUtil.generarAccessToken(usuario.getNombreDeUsuario());
            System.out.println(">>> BACKEND: Access Token generado");

            // Si tu JwtUtil tiene refresh token, úsalo, sino comenta esta línea
            // String refreshToken = jwtUtil.generarRefreshToken(usuario.getNombreDeUsuario());

            AuthResponseDTO resp = new AuthResponseDTO("Bearer", accessToken, 3600);
            return ResponseEntity.ok(resp);

        } catch (NotFoundException e) {
            System.err.println(">>> BACKEND ERROR 401: " + e.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        } catch (Exception e) {
            // 👇 ESTO ES LO IMPORTANTE: Imprimir el stack trace completo
            System.err.println(">>> BACKEND ERROR 500 FATAL:");
            e.printStackTrace();

            String msg = e.getMessage() != null ? e.getMessage() : "Error desconocido (NullPointerException?)";
            return ResponseEntity.status(500).body(Map.of("error", "Error interno", "detail", msg));
        }
    }
}