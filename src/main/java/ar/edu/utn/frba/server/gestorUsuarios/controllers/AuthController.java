package ar.edu.utn.frba.server.gestorUsuarios.controllers;

import ar.edu.utn.frba.server.gestorUsuarios.dtos.LoginRequest;
import ar.edu.utn.frba.server.gestorUsuarios.dtos.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());

            Authentication auth = authenticationManager.authenticate(token);

            // Guarda el SecurityContext en la sesión (JSESSIONID)
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            request.getSession(true); // crea sesión si no existe
            new HttpSessionSecurityContextRepository().saveContext(context, request, response);

            // Opcional: devolver un dto simple del usuario
            User principal = (User) auth.getPrincipal();
            return ResponseEntity.ok(new UserDto(principal.getUsername()));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).body("No autenticado");
        return ResponseEntity.ok(new UserDto(user.getUsername()));
    }

    // Logout lo maneja SecurityConfig en /auth/logout (POST)
    // Pero si querés un GET/POST manual:
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession(false); // si hay
        // Spring ya hace invalidate en el logout configurado, acá sería redundante
        return ResponseEntity.ok().build();
    }
}