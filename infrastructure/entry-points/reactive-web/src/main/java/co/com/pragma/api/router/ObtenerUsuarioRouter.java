package co.com.pragma.api.router;

import co.com.pragma.api.dto.response.UsuarioDTO;
import co.com.pragma.api.handlers.Handler;
import co.com.pragma.model.usuario.errores.ErrorValidacion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class ObtenerUsuarioRouter {

    public static final String PATH = "/api/v1/usuarios/documento/{documentoId}";
    private final Handler handler;

    @Bean
    @RouterOperation(
            path = PATH,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            method = RequestMethod.GET,
            beanClass = Handler.class,
            beanMethod = "obtenerUsuarioPorDocumentoId",
            operation = @Operation(
                    operationId = "obtenerUsuarioPorDocumentoId",
                    tags = {"Usuarios"},
                    summary = "Obtiene un usuario por su número de documento",
                    parameters = {
                            @Parameter(
                                    in = ParameterIn.PATH,
                                    name = "documentoId",
                                    description = "Número de documento del usuario",
                                    required = true,
                                    schema = @Schema(type = "integer", format = "int64")
                            )
                    },
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Usuario encontrado exitosamente",
                                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))
                            ),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Usuario no encontrado",
                                    content = @Content(
                                            schema = @Schema(implementation = ErrorValidacion.class),
                                            examples = @ExampleObject(
                                                    name = "Ejemplo de respuesta de error 404",
                                                    value = "{\"status\": 404,\"message\": \"Usuario no encontrado\",\"errors\": []}"
                                            )
                                    )
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> obtenerUsuarioRouterFunction() {
        return RouterFunctions.route(RequestPredicates.GET(PATH)
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::obtenerUsuarioPorDocumentoId);
    }

}
