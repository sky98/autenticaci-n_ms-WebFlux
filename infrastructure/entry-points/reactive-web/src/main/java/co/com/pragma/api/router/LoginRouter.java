package co.com.pragma.api.router;

import co.com.pragma.api.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
    public RouterFunction<ServerResponse> loginRouterFunction(){
        return RouterFunctions.route(POST(PATH)
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::login
        );
    }
}
