package co.com.pragma.api.config;

import co.com.pragma.api.security.AccessDeniedHandler;
import co.com.pragma.api.security.AuthenticationEntryPoint;
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
    public String PATH_SWAGGER = "/swagger-ui.html,/swagger-ui/,/webjars/swagger-ui/**";

    private final RequestTokenFilter requestTokenFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                PATH_LOGIN,
                                PATH_SWAGGER
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(requestTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }

}
