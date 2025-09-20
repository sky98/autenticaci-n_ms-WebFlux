package co.com.pragma.api.config;

import co.com.pragma.api.handlers.AccessDeniedHandler;
import co.com.pragma.api.handlers.AuthenticationEntryPoint;
import co.com.pragma.api.security.JwtAuthenticateManager;
import co.com.pragma.api.security.RequestTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityHeadersConfig /*implements WebFilter*/ {

    public String PATH_OBTENER_USUARIO_DOCUMENTO_ID = "/api/v1/usuarios/documento/{documentoId}";
    public String PATH_VALIDAR_USUARIO_POR_DOCUMENTO_ID = "/api/v1/usuarios/documento/{documentoId}/existe";
    public String PATH_LOGIN = "/api/v1/login";
    public String PATH_SWAGGER = "/swagger-ui/**";
    public String PATH_API_DOCS = "/v3/api-docs/**";
    public String PATH_ACTUATOR_HEALTH = "/actuator/health";

    private final RequestTokenFilter requestTokenFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticateManager jwtAuthenticateManager;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authenticationManager(jwtAuthenticateManager)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                PATH_ACTUATOR_HEALTH,
                                PATH_SWAGGER,
                                PATH_API_DOCS,
                                PATH_LOGIN,
                                PATH_VALIDAR_USUARIO_POR_DOCUMENTO_ID,
                                PATH_OBTENER_USUARIO_DOCUMENTO_ID
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
