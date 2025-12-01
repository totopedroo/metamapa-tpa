
package ar.edu.utn.frba.server.config;

import ar.edu.utn.frba.server.gestorUsuarios.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
// 👇 Estos imports son vitales para el AuthenticationManager
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
                    auth.requestMatchers("/hechos/**").permitAll();
                    auth.requestMatchers("/fuente-dinamica/**").permitAll();
                    auth.requestMatchers("/fuente-estatica/**").permitAll();

                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.requestMatchers("/solicitudes/**").permitAll();


                    // Registro Público
                    auth.requestMatchers(HttpMethod.POST, "/usuarios/register").permitAll();

                    // Datos públicos para la Landing Page
                    auth.requestMatchers(HttpMethod.GET, "/api/colecciones/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/hechos/**").permitAll();

                    auth.requestMatchers(HttpMethod.PUT, "/api/colecciones/**").permitAll();
                    auth.requestMatchers(HttpMethod.PATCH, "/api/colecciones/**").permitAll();
                    auth.requestMatchers(HttpMethod.DELETE, "/api/colecciones/**").permitAll();

                    // Swagger / H2 Console (Opcional)
                    auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/h2-console/**").permitAll();

                    // Todo lo demás requiere Token
                    auth.anyRequest().authenticated();
                })
                // 4. Filtro JWT
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Configuración extra para H2 Console (si la usas)
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}