package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface GestorCredencialesPort {
    Usuario encryptarContrasena(Usuario usuario);
    Mono<Boolean> validarContrasena(String contrasena, Usuario usuario);
}
