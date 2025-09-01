package co.com.pragma.usecase.gestorsesion;

import co.com.pragma.model.usuario.errores.ErrorDominio;
import co.com.pragma.model.usuario.gateways.GestorCredencialesPort;
import co.com.pragma.model.usuario.gateways.JwtUtilsPort;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Set;

@RequiredArgsConstructor
public class GestorSesionUseCase {

    private final UsuarioRepository usuarioRepository;
    private final GestorCredencialesPort gestorCredencialesPort;
    private final JwtUtilsPort jwtUtilsPort;

    public Mono<String> Login(String correoElectronico, String contrasena){
        return usuarioRepository.obtenerPorCorreo(correoElectronico)
                .switchIfEmpty(Mono.error(new ErrorDominio("Credenciales invalidas", Set.of("Login:invalid"))))
                .flatMap(usuario -> gestorCredencialesPort.validarContrasena(contrasena, usuario)
                        .filter(Boolean::booleanValue)
                        .map(isValid -> usuario)
                        .switchIfEmpty(Mono.error(new ErrorDominio("Credenciales invalidas", Set.of("Login:invalid"))))
                )
                .flatMap(usuario -> jwtUtilsPort.generarToken(usuario.getCorreoElectronico(), usuario.getRolId(), usuario.getDocumentoId()));
    }

}
