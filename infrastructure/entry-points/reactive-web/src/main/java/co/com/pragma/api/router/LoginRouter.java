package co.com.pragma.api.router;

import co.com.pragma.api.dto.request.LoginRequest;
import co.com.pragma.api.dto.response.LoginResponse;
import co.com.pragma.api.handlers.Handler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
@RequiredArgsConstructor
public class LoginRouter {

    public static final String PATH = "/api/v1/login";
    private final Handler handler;

    @Bean
    @RouterOperation(
            path = PATH,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            method = RequestMethod.POST,
            beanClass = Handler.class,
            beanMethod = "login",
            operation = @Operation(
                    operationId = "login",
                    tags = {"Autenticacion"},
                    summary = "Inicio de sesión de usuario",
                    description = "Autentica a un usuario y retorna un token JWT para acceder a los recursos protegidos.",
                    requestBody = @RequestBody(
                            description = "Credenciales del usuario",
                            required = true,
                            content = @Content(
                                    schema = @Schema(implementation = LoginRequest.class)
                            )
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Login exitoso",
                                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Credenciales inválidas o solicitud incorrecta",
                                    content = @Content(schema = @Schema(implementation = Exception.class))
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> loginRouterFunction(){
        return RouterFunctions.route(POST(PATH)
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::login
        );
    }
}
