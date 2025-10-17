package ar.edu.utn.frba.server.config;


import ar.edu.utn.frba.server.gestorUsuarios.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // habilita @PreAuthorize/@Secured
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter,
                          UserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // Rutas públicas (landing, login, vistas de lectura)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index", "/landing", "/legal/**", "/privacy/**"
                        ).permitAll()
                        .requestMatchers(
                                "/api/auth/**"  // login/refresh/register
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/register",
                                "/solicitudes").permitAll()

                        // Visualización anónima de hechos/colecciones/estadísticas/export
                        .requestMatchers(HttpMethod.GET,

                                "/hechos",
                                "/api/estadisticas/**",
                                "/api/export/**",
                                "/api/busqueda/**",
                                "/colecciones/{coleccionId}/hechos/filtrados",
                                "/colecciones/{id}/hechos/navegacion",
                                "/texto-libre"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                        "/colecciones").hasAnyRole("ADMIN")

                        // Contribuyente: crear/editar hechos propios, crear solicitudes
                        .requestMatchers(HttpMethod.POST,
                                "/crear",
                                "/fuente-dinamica/hechos/{hechoId}"
                                )
                                .hasAnyRole("CONTRIBUYENTE")
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

                        .requestMatchers(HttpMethod.PUT,
                                "/colecciones/{id}/algoritmo"

                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH,
                                "/colecciones/{coleccionId}/hechos/{hechoId}/quitar-fuente",
                                "/colecciones/{coleccionId}/hechos/{hechoId}/agregar-fuente",
                                "/solicitudes/{id}"

                        ).hasRole("ADMIN")

                                .requestMatchers(HttpMethod.POST,
                                        "/fuente-estatica/importar-csv"


                                ).hasRole("ADMIN")




                        // cualquier otra cosa: autenticado
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}