package co.com.pragma.usecase.usuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository repository;

    public Mono<Usuario> guardar(Usuario usuario){
        return repository.guardar(usuario);
    }

    public Mono<Usuario> buscarPorId(String idUsuario){
        return repository.buscarPorId(idUsuario);
    }

}
