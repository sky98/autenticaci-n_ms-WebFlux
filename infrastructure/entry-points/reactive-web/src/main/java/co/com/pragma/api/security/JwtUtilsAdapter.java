package co.com.pragma.api.security;

import co.com.pragma.model.usuario.gateways.JwtUtilsPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtilsAdapter implements JwtUtilsPort {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration.millis}")
    private Long jwtExpirationMillis;

    @Override
    public Mono<String> generarToken(String claim, Long rolId) {
        log.info("Generando token.");
        return Mono.fromCallable(() -> Jwts.builder()
                .setSubject(claim)
                .claim("rolId", rolId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact());

    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

}
