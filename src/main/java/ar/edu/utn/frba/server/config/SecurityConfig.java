
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

    // 👇👇👇 ESTE ES EL BEAN QUE SOLUCIONA TU ERROR DE INICIO 👇👇👇
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
                .cors(Customizer.withDefaults())   // 👈 habilitar CORS

                // 2. Política Stateless (Sin sesiones en el servidor)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Reglas de acceso
                .authorizeHttpRequests(auth -> {
                    // Auth y Login
                    auth.requestMatchers("/hechos/**").permitAll();
                    auth.requestMatchers("/fuente-dinamica/**").permitAll();
                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.requestMatchers("/solicitudes").permitAll();


                    // Registro Público
                    auth.requestMatchers(HttpMethod.POST, "/usuarios/register").permitAll();

                    // Datos públicos para la Landing Page
                    auth.requestMatchers(HttpMethod.GET, "/api/colecciones/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/hechos/**").permitAll();

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




/*package ar.edu.utn.frba.server.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager; // Importación necesaria
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Importación necesaria
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // NUEVO BEAN: Esto expone el AuthenticationManager para que AuthController lo pueda inyectar
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF: Obligatorio cuando se usa anyRequest().permitAll() o CORS complejo
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configurar CORS (usamos la configuración del bean de abajo)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Permitir TODAS las peticiones (incluye /fuente-dinamica/**)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    // Bean para configurar CORS globalmente (solo si no usas @CrossOrigin en todos los Controllers)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // **IMPORTANTE**: Permitir solo el origen 8082
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8082"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

/*
import ar.edu.utn.frba.server.gestorUsuarios.services.CustomUserDetailsService;
=======
import ar.edu.utn.frba.server.gestorUsuarios.filters.JwtAuthenticationFilter;
>>>>>>> d23349f570260998e2786b6a20df3636b583a1dc
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
// 👇 Estos imports son vitales para el AuthenticationManager
import org.springframework.security.authentication.AuthenticationManager;
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

        // 2. Política Stateless (Sin sesiones en el servidor)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

<<<<<<< HEAD
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Para API: desactivo CSRF por simplicidad (si querés, activalo con CookieCsrfTokenRepository)
                /*.csrf(csrf -> csrf.disable())
=======
        // 3. Reglas de acceso
        .authorizeHttpRequests(auth -> {
          // Auth y Login
          auth.requestMatchers("/api/auth/**").permitAll();
>>>>>>> d23349f570260998e2786b6a20df3636b583a1dc

          // Registro Público
          auth.requestMatchers(HttpMethod.POST, "/usuarios/register").permitAll();

          // Datos públicos para la Landing Page
          auth.requestMatchers(HttpMethod.GET, "/api/colecciones/**").permitAll();
          auth.requestMatchers(HttpMethod.GET, "/api/hechos/**").permitAll();

          // Swagger / H2 Console (Opcional)
          auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/h2-console/**").permitAll();

          // Todo lo demás requiere Token
          auth.anyRequest().authenticated();
        })
        // 4. Filtro JWT
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

<<<<<<< HEAD
        // si usás h2-console
        http.headers(h -> h.frameOptions(frame -> frame.sameOrigin()));*/
/*.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll());
        return http.build();
    }
}*/
/*+    .requestMatchers(
                                "/", "/index", "/landing", "/legal/**", "/privacy/**"
                        ).permitAll()
                        .requestMatchers(
                                "/api/auth/**"  // login/refresh/register
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/register").permitAll()
                        // Visualización anónima de hechos/colecciones/estadísticas/export
                        .requestMatchers(HttpMethod.GET,
                                "/colecciones",
                                "/hechos",
                                "/api/estadisticas/**",
                                "/api/export/**",
                                "/api/busqueda/**"
                        ).hasAnyRole("ADMIN")


                        // Contribuyente: crear/editar hechos propios, crear solicitudes
                        .requestMatchers(HttpMethod.POST,
                                "/hechos/**",
                                "/solicitudes/**"
                        ).hasAnyRole("CONTRIBUYENTE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/hechos/**")
                        .hasAnyRole("CONTRIBUYENTE", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/hechos/**")
                        .hasAnyRole("CONTRIBUYENTE", "ADMIN")

                        // Admin: gestión de colecciones, fuentes, normalizador, aprobaciones, import masivo
                        .requestMatchers(
                                "/colecciones/admin/**",
                                "/servicio-agregador/**",
                                "/normalizador/**"
                        ).hasRole("ADMIN")

                        // cualquier otra cosa: autenticado
                        .anyRequest().authenticated()*/
/*=======
    // Configuración extra para H2 Console (si la usas)
    http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

    return http.build();
  }
}*/