package co.com.pragma.api.security;

import co.com.pragma.model.usuario.gateways.JwtUtilsPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtilsAdapter implements JwtUtilsPort {


    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration.millis}")
    private Long jwtExpirationMillis;

    public SecretKey jwtSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Mono<String> generarToken(String claim, Long rolId, Long documentoId) {
        log.info("Generando token.");
        return Mono.fromCallable(() -> Jwts.builder()
                .subject(claim)
                .claim("rolId", rolId)
                .claim("documentId", documentoId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMillis))
                .signWith(jwtSecretKey())
                .compact());

    }

    public Mono<String> getUsernameFromToken(String token) {
        log.info("Obteniendo usernme del token");
        return Mono.fromCallable(() -> Jwts.parser()
                .verifyWith(jwtSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public Claims getClaimsFromToken(String token) {
        log.info("Obteniendo username del token");
        return Jwts.parser()
                .verifyWith(jwtSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaimsFromToken(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException ex) {
            log.error("The JWT token has expired: {}", ex.getMessage());
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            return true;
        }
    }


}
