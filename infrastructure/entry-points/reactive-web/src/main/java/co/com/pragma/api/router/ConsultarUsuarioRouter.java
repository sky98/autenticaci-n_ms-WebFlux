package co.com.pragma.api.router;

import co.com.pragma.api.Handler;
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
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class ConsultarUsuarioRouter {

    public static final String PATH = "/api/v1/usuarios/documento/{documentoId}";
    private final Handler handler;

    @Bean
    @RouterOperation(
            path = PATH,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            method = org.springframework.web.bind.annotation.RequestMethod.GET,
            beanClass = Handler.class,
            beanMethod = "existeUsuarioPorDocumento",
            operation = @Operation(
                    operationId = "existeUsuarioPorDocumento",
                    tags = {"Usuarios"},
                    summary = "Verifica si un usuario existe por su documento de identidad",
                    parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "documentoId", description = "ID del documento a buscar", required = true, schema = @Schema(type = "integer", format = "int64"))
                    },
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Consulta exitosa. El cuerpo de la respuesta indica si el usuario existe.",
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            examples = {
                                                    @ExampleObject(name = "Usuario existe", value = "{\"existe\": true}"),
                                                    @ExampleObject(name = "Usuario no existe", value = "{\"existe\": false}")
                                            }
                                    )
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> consultaUsuarioRouterFunction() {
        return RouterFunctions.route(RequestPredicates.GET(PATH)
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::existeUsuarioPorDocumento);
    }
}
