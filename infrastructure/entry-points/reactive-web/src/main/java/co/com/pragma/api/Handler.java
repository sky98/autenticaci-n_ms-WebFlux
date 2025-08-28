package co.com.pragma.api;

import co.com.pragma.api.dto.request.CrearUsuarioDTO;
import co.com.pragma.api.mapper.UsuarioDTOMapper;
import co.com.pragma.usecase.consultarusuario.ConsultarUsuarioUseCase;
import co.com.pragma.usecase.usuario.CrearUsuarioUseCase;
import io.netty.handler.codec.http.HttpResponseStatus;
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
    private final ConsultarUsuarioUseCase consultarUsuarioUseCase;
    private final UsuarioDTOMapper mapper;
    private final ValidadorRequest validador;

    public Mono<ServerResponse> guardarUsuario(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CrearUsuarioDTO.class)
                .doOnNext(dto -> log.info("Iniciando flujo de guardar usuario : {}", dto))
                .flatMap(validador::validar)
                .map(mapper::toModel)
                .flatMap(crearUsuarioUseCase::guardar)
                .map(mapper::toResponse)
                .doOnSuccess(responseDTO -> {
                    log.info("Usuario guardado con exito : {}", responseDTO);
                })
                .flatMap(response -> ServerResponse.status(HttpResponseStatus.CREATED.code())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                )
                .doOnError(e -> log.error("Error al guardar usuario : {}", e.getMessage()));
    }

    public Mono<ServerResponse> existeUsuarioPorDocumento(ServerRequest serverRequest){
        String documentoId = serverRequest.pathVariable("documentoId");
        log.info("Iniciando flujo de consultar usuario por documentoId : {}", documentoId);
        return consultarUsuarioUseCase.existeUsuarioPorDocumentoActivo(Long.valueOf(documentoId))
                .flatMap(
                        existe -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{\"existe\": " + existe + "}")
                );
    }
}
