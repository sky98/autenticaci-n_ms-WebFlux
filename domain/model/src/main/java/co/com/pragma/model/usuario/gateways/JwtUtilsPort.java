package co.com.pragma.model.usuario.gateways;

import reactor.core.publisher.Mono;

public interface JwtUtilsPort {
    Mono<String> generarToken(String claim, Long rolId);
}
