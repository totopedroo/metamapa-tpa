package ar.edu.utn.frba.server.config;

import ar.edu.utn.frba.server.gestorUsuarios.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtFilter;

  // Agregamos este Bean (del conflicto de abajo) porque es útil para inyectar en tus servicios
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Desactivamos CSRF porque usamos JWT (Stateless)
        .csrf(AbstractHttpConfigurer::disable)

        // Definimos política sin estado (No crea JSESSIONID)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .authorizeHttpRequests(auth -> {
          // 1. Login y Auth
          auth.requestMatchers("/api/auth/**").permitAll();

          // 2. Registro de Usuarios (CRUCIAL para lo que estamos haciendo)
          auth.requestMatchers(HttpMethod.POST, "/usuarios/register").permitAll();

          // 3. Rutas Públicas de Hechos y Colecciones (para tu Landing Page)
          auth.requestMatchers(HttpMethod.GET, "/api/colecciones").permitAll();
          auth.requestMatchers(HttpMethod.GET, "/api/hechos").permitAll();

          // 4. Opcional: Permitir Swagger/H2 si lo usas (estaba en tu otro branch)
          auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/h2-console/**").permitAll();

          // 5. Cualquier otra cosa requiere Token
          auth.anyRequest().authenticated();
        })
        // Añadimos el filtro JWT antes del de usuario/contraseña
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // Configuración extra para H2 Console si la usas
    http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

    return http.build();
  }
}