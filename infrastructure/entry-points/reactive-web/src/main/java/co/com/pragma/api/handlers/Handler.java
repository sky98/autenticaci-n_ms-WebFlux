package co.com.pragma.api.handlers;

import co.com.pragma.api.ValidadorRequest;
import co.com.pragma.api.dto.request.CrearUsuarioDTO;
import co.com.pragma.api.dto.request.LoginRequest;
import co.com.pragma.api.dto.response.LoginResponse;
import co.com.pragma.api.mapper.UsuarioDTOMapper;
import co.com.pragma.usecase.consultarusuario.ConsultarUsuarioUseCase;
import co.com.pragma.usecase.gestorsesion.GestorSesionUseCase;
import co.com.pragma.usecase.obtenerusuario.ObtenerUsuarioUseCase;
import co.com.pragma.usecase.usuario.CrearUsuarioUseCase;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final GestorSesionUseCase gestorSesionUseCase;
    private final ObtenerUsuarioUseCase obtenerUsuarioUseCase;
    private final UsuarioDTOMapper mapper;
    private final ValidadorRequest validador;

    @PreAuthorize("hasRole('1') or hasRole('2')")
    public Mono<ServerResponse> guardarUsuario(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CrearUsuarioDTO.class)
                .doOnNext(dto -> log.info("Iniciando flujo de guardar usuario : {}", dto))
                .flatMap(validador::validar)
                .map(mapper::toModel)
                .flatMap(crearUsuarioUseCase::guardar)
                .map(mapper::toResponse)
                .flatMap(response -> {
                    log.info("Usuario guardado con exito :{}", response);
                            return ServerResponse.status(HttpResponseStatus.CREATED.code())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(response);
                        }
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

    public Mono<ServerResponse> login(ServerRequest serverRequest){
        log.info("Iniciando flujo de login de usuario");
        return serverRequest.bodyToMono(LoginRequest.class)
                .flatMap(validador::validar)
                .flatMap(request -> gestorSesionUseCase.Login(request.correoElectronico(), request.contrasena()))
                .map(LoginResponse::new)
                .flatMap(
                        response -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response)
                );
    }

    public Mono<ServerResponse> obtenerUsuarioPorDocumentoId(ServerRequest serverRequest){
        return Mono.just(serverRequest.pathVariable("documentoId"))
                .flatMap(documentoId -> {
                    log.info("Iniciando flujo de obtener usuario por documento id : {}", documentoId);
                    return obtenerUsuarioUseCase.obtenerUsuarioPorDocumentoId(Long.valueOf(documentoId));
                })
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                );

    }

    public Mono<ServerResponse> checkHealth(ServerRequest serverRequest){
        log.info("Iniciando flujo de health check");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"status\": \"UP\"}");
    }

}
