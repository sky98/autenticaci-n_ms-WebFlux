package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    Mono<Usuario> guardar(Usuario usuario);
    Mono<Usuario> buscarPorId(String idUsuario);

}
