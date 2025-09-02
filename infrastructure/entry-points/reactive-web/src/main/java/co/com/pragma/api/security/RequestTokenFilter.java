package co.com.pragma.api.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestTokenFilter implements WebFilter {

    private final ReactiveUserDetailsServiceImpl reactiveUserDetailsService;
    private final JwtUtilsAdapter jwtUtilsAdapter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("Se dispara RequestTokenFilter");
        return extractJwt(exchange)
                .flatMap(jwt -> authenticateUser(jwt, exchange, chain))
                .switchIfEmpty(chain.filter(exchange));
    }

    private Mono<String> extractJwt(ServerWebExchange exchange) {
        log.info("Extrayecto token del header de autenticacion");
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Mono.just(authHeader.substring(7));
        }
        return Mono.empty();
    }

    private Mono<Void> authenticateUser(String jwt, ServerWebExchange exchange, WebFilterChain chain) {
        log.info("Autenticando usuario en contexto de Spring");
        return jwtUtilsAdapter.getUsernameFromToken(jwt)
                .doOnSuccess(username -> log.info("Se extrajo con exito el username del token : {}", username))
                .flatMap(reactiveUserDetailsService::findByUsername)
                .flatMap(userDetails -> createAuthenticationAndContinueChain(userDetails, exchange, chain))
                .switchIfEmpty(chain.filter(exchange));
    }

    private Mono<Void> createAuthenticationAndContinueChain(UserDetails userDetails, ServerWebExchange exchange, WebFilterChain chain) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }
}
