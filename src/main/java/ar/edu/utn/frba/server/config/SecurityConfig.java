package ar.edu.utn.frba.server.config; // Ajusta el paquete si es necesario

import ar.edu.utn.frba.server.gestorUsuarios.filters.JwtAuthenticationFilter; // Asegúrate de tener este filtro
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  // Inyectamos tu filtro JWT (asumo que lo tienes creado o instálalo como bean)
  // Si no lo tienes como Bean, puedes hacer new JwtAuthenticationFilter() en el addFilterBefore si no tiene dependencias
  private final JwtAuthenticationFilter jwtFilter;

  public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> {
          // 1. Rutas de Autenticación (Login)
          auth.requestMatchers("/api/auth/**").permitAll();

          // 2. Ruta de Registro (PÚBLICA)
          auth.requestMatchers(HttpMethod.POST, "/usuarios/register").permitAll();

          // 3. Rutas Públicas de Hechos y Colecciones (para tu Landing)
          auth.requestMatchers(HttpMethod.GET, "/api/colecciones").permitAll();
          auth.requestMatchers(HttpMethod.GET, "/api/hechos").permitAll();

          // 4. Cualquier otra cosa requiere Token
          auth.anyRequest().authenticated();
        })
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}