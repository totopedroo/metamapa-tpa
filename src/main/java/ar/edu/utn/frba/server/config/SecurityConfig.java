package ar.edu.utn.frba.server.config;

import ar.edu.utn.frba.server.gestorUsuarios.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Permite que AuthController pueda inyectar AuthenticationManager
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http

        // 1. Desactivamos CSRF (No necesario para APIs Stateless)
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())

        // 2. Política Stateless (Sin sesiones en el servidor)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // 3. Reglas de acceso
        .authorizeHttpRequests(auth -> {
          // Auth y Login
          auth.requestMatchers("/api/auth/**").permitAll();

          // Registro Público
          auth.requestMatchers(HttpMethod.POST, "/usuarios/register").permitAll();

          // Datos públicos para la Landing Page
          auth.requestMatchers(HttpMethod.GET, "/api/colecciones/**").permitAll();
          auth.requestMatchers(HttpMethod.GET, "/api/hechos/**").permitAll();
          auth.requestMatchers(HttpMethod.GET, "/api/criterios").permitAll();
          auth.requestMatchers(HttpMethod.POST, "/fuente-dinamica/hechos/crear").permitAll();

          auth.requestMatchers(HttpMethod.POST, "/api/colecciones/**").permitAll();
          auth.requestMatchers(HttpMethod.PUT, "/api/colecciones/**").permitAll();
          auth.requestMatchers(HttpMethod.PATCH, "/api/colecciones/**").permitAll();
          auth.requestMatchers(HttpMethod.DELETE, "/api/colecciones/**").permitAll();

          // Swagger / H2 Console (Opcional)
          auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/h2-console/**").permitAll();

          // Reglas generales (deben ir después de las específicas)
          auth.requestMatchers(HttpMethod.GET, "/api/**").permitAll();
          auth.requestMatchers(HttpMethod.POST, "/api/**").authenticate;
          auth.requestMatchers(HttpMethod.PUT,  "/api/**").authenticated();
          auth.requestMatchers(HttpMethod.PATCH,"/api/**").authenticated();
          auth.requestMatchers(HttpMethod.DELETE,"/api/**").authenticated();


          auth.anyRequest().authenticated();
        })

        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

    return http.build();
  }
}