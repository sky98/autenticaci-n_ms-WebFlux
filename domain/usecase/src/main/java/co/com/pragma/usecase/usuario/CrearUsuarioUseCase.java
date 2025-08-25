package co.com.pragma.usecase.usuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.errores.ErrorDominio;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Set;

@RequiredArgsConstructor
public class CrearUsuarioUseCase {

    private final UsuarioRepository repository;

    public Mono<Usuario> guardar(Usuario usuario){
        return repository.existeCorreoElectronico(usuario.getCorreoElectronico())
                .flatMap(existe -> existe
                        ? Mono.error(new ErrorDominio("Correo no esta disponible", Set.of("correo_electronico")))
                        : Mono.just(usuario))
                .flatMap(repository::guardar);
    }

}
