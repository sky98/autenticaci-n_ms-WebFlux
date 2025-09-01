package co.com.pragma.api.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecretKey jwtSecretKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
