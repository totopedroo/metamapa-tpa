package ar.edu.utn.frba.server.gestorUsuarios.filters;

import ar.edu.utn.frba.server.gestorUsuarios.services.CustomUserDetailsService;
import ar.edu.utn.frba.server.gestorUsuarios.services.TokenService;
import ar.edu.utn.frba.server.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokens;
    private final CustomUserDetailsService users;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String jwt = auth.substring(7);
            try {
                var claims = tokens.parse(jwt).getBody();
                var user = users.loadUserByUsername(claims.getSubject());
                UsernamePasswordAuthenticationToken at =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                at.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(at);
            } catch (Exception ignored) { /* caerá 401/403 según config */ }
        }
        chain.doFilter(req,res);
    }
}