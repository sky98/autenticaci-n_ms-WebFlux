package co.com.pragma.api.security;

import co.com.pragma.model.usuario.gateways.JwtUtilsPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtilsAdapter implements JwtUtilsPort {

    private final SecretKey jwtSecretKey;

    @Value("${jwt.expiration.millis}")
    private Long jwtExpirationMillis;

    @Override
    public Mono<String> generarToken(String claim, Long rolId, Long documentoId) {
        log.info("Generando token.");
        return Mono.fromCallable(() -> Jwts.builder()
                .subject(claim)
                .claim("rolId", rolId)
                .claim("documentId", documentoId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMillis))
                .signWith(jwtSecretKey)
                .compact());

    }

    public Mono<String> getUsernameFromToken(String token) {
        log.info("Obteniendo usernme del token");
        return Mono.fromCallable(() -> Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public Claims getClaimsFromToken(String token) {
        log.info("Obteniendo usernme del token");
        return Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Mono<Boolean> validarToken(String authToken) {
        log.info("validando token");
        return Mono.just(authToken)
                .flatMap(token -> {
                    try {
                        Jwts.parser()
                                .verifyWith(jwtSecretKey)
                                .build()
                                .parseSignedClaims(token);
                        return Mono.just(true);
                    } catch (SignatureException ex) {
                        log.error("Invalid JWT signature: {}", ex.getMessage());
                    } catch (MalformedJwtException ex) {
                        log.error("Malformed JWT token: {}", ex.getMessage());
                    } catch (ExpiredJwtException ex) {
                        log.error("The JWT token has expired: {}", ex.getMessage());
                    } catch (UnsupportedJwtException ex) {
                        log.error("The JWT token is not supported: {}", ex.getMessage());
                    } catch (IllegalArgumentException ex) {
                        log.error("The JWT claims string is empty: {}", ex.getMessage());
                    }
                    return Mono.just(false);
                });
    }

}
