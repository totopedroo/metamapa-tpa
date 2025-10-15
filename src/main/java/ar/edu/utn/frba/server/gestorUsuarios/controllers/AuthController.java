package ar.edu.utn.frba.server.gestorUsuarios.controllers;

import ar.edu.utn.frba.server.config.NotFoundException;
import ar.edu.utn.frba.server.gestorUsuarios.dtos.*;
import ar.edu.utn.frba.server.gestorUsuarios.services.LoginService;
import ar.edu.utn.frba.server.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder enc =
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    @PostMapping(value="/login", consumes="application/json", produces="application/json")
    public ResponseEntity<?> loginApi(@RequestBody Map<String,String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username y password son obligatorios"));
        }
        try {
            var usuario = loginService.autenticarUsuario(username, password);

            String accessToken = jwtUtil.generarAccessToken(usuario.getNombreDeUsuario());
            String refreshToken = jwtUtil.generarRefreshToken(usuario.getNombreDeUsuario());

            // Si tu DTO no tiene refresh, devolvé sólo los 3 campos o agregá refresh al DTO
            AuthResponseDTO resp = new AuthResponseDTO("Bearer", accessToken, 3600);
            return ResponseEntity.ok(resp);

        } catch (NotFoundException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno", "detail", e.getMessage()));
        }
    }

    @GetMapping("/check")
    public Map<String,Object> check(@RequestParam String raw, @RequestParam String hash) {
        boolean ok = enc.matches(raw, hash);
        return Map.of("raw", raw, "hash_len", hash.length(), "matches", ok);
    }

    @GetMapping("/make")
    public Map<String,String> make(@RequestParam String raw) {
        return Map.of("hash", enc.encode(raw));
    }
}
  /*
  @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        try {
            String username = JwtUtil.validarToken(request.getRefreshToken());

            // Validar que el token sea de tipo refresh
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getKey())
                    .build()
                    .parseClaimsJws(request.getRefreshToken())
                    .getBody();

            if (!"refresh".equals(claims.get("type"))) {
                return ResponseEntity.badRequest().build();
            }

            String newAccessToken = JwtUtil.generarAccessToken(username);
            TokenResponse response = new TokenResponse(newAccessToken, request.getRefreshToken());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
*/
/*
    @GetMapping("/user/roles-permisos")
    public ResponseEntity<UserRolesPermissionsDTO> getUserRolesAndPermissions(Authentication authentication) {
        try {
            String username = authentication.getName();
            UserRolesPermissionsDTO response = loginService.obtenerRolesYPermisosUsuario(username);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Usuario no encontrado", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener roles y permisos del usuario", e);
            return ResponseEntity.badRequest().build();
        }
    }

*/