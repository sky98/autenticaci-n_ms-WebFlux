package co.com.pragma.api.config;

import co.com.pragma.api.security.RequestTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityHeadersConfig /*implements WebFilter*/ {

    public String PATH_CREAR_USUARIO = "/api/v1/usuarios";
    public String PATH_VALIDAR_USUARIO_POR_DOCUMENTO_ID = "/api/v1/usuarios/documento/{documentoId}/existe";
    public String PATH_LOGIN = "/api/v1/login";

    private final RequestTokenFilter requestTokenFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                PATH_LOGIN
                                //PATH_VALIDAR_USUARIO_POR_DOCUMENTO_ID
                                /*,*/
                                //PATH_CREAR_USUARIO
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(requestTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }

}
