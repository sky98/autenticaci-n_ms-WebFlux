package co.com.pragma.api;

import co.com.pragma.api.dto.request.CrearUsuarioDTO;
import co.com.pragma.api.mapper.UsuarioDTOMapper;
import co.com.pragma.usecase.usuario.CrearUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final UsuarioDTOMapper mapper;
    private final ValidadorRequest validador;

    public Mono<ServerResponse> guardarUsuario(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CrearUsuarioDTO.class)
                .doOnNext(dto -> log.info("Iniciando flujo de guardar usuario : {}", dto))
                .flatMap(validador::validar)
                .map(mapper::toModel)
                .flatMap(crearUsuarioUseCase::guardar)
                .flatMap(guardarUsuario -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(guardarUsuario)
                )
                .doOnError(e -> log.error("Error al guardar usuario : {}", e.getMessage()));
    }
}
