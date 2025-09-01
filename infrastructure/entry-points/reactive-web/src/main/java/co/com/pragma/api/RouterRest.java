package co.com.pragma.api;

import co.com.pragma.api.router.ConsultarUsuarioRouter;
import co.com.pragma.api.router.CrearUsuarioRouter;
import co.com.pragma.api.router.LoginRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final CrearUsuarioRouter crearUsuarioRouter;
    private final ConsultarUsuarioRouter consultarUsuarioRouter;
    private final LoginRouter loginRouter;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .add(crearUsuarioRouter.crearUsuarioRouterFunction())
                .add(consultarUsuarioRouter.consultaUsuarioRouterFunction())
                .add(loginRouter.loginRouterFunction())
                .build();
    }
}
