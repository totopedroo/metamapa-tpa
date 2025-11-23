package ar.edu.utn.frba.server.config;


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
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usá el mismo encoder que empleaste al crear usuarios
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder encoder,
                                                               UserDetailsService userDetailsService) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setPasswordEncoder(encoder);
        p.setUserDetailsService(userDetailsService);
        return p;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider provider) {
        // AuthenticationManager para login manual en el controller
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Para API: desactivo CSRF por simplicidad (si querés, activalo con CookieCsrfTokenRepository)
                /*.csrf(csrf -> csrf.disable())

                // CON SESIONES: nada de STATELESS
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        // si tenés swagger/h2:
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // permitir GET público a algo, si querés:
                        .requestMatchers(HttpMethod.GET, "/public/**").permitAll()
                        .anyRequest().authenticated()
                )

                // No usamos formLogin tradicional (login será por endpoint JSON)
                .formLogin(form -> form.disable())

                // Logout estándar en /logout (POST por default en Spring Security 6)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(200))
                );

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
