package ar.edu.utn.frba.server.gestorUsuarios.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value; // ✅

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.access.expiration-ms}") private long accessMs;
    @Value("${jwt.refresh.expiration-ms}") private long refreshMs;

    public String generateAccess(String username, Collection<? extends GrantedAuthority> auths){
        Date now = new Date(), exp = new Date(now.getTime()+accessMs);
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", auths.stream().map(GrantedAuthority::getAuthority).toList())
                .setIssuedAt(now).setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
    public String generateRefresh(String username){
        Date now=new Date(), exp=new Date(now.getTime()+refreshMs);
        return Jwts.builder()
                .setSubject(username).setIssuedAt(now).setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).compact();
    }
    public Jws<Claims> parse(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build().parseClaimsJws(token);
    }
}