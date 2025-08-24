package co.com.pragma.api;

import co.com.pragma.api.dto.request.CrearUsuarioDTO;
import co.com.pragma.api.mapper.UsuarioDTOMapper;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.usuario.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioDTOMapper mapper;
//private  final UseCase2 useCase2;

    public Mono<ServerResponse> guardarUsuario(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CrearUsuarioDTO.class)
                .map(mapper::toModel)
                .flatMap(usuarioUseCase::guardar)
                .flatMap(guardarUsuario -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(guardarUsuario)
                );
    }

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
}
