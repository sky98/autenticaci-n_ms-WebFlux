package co.com.pragma.usecase.obtenerusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ObtenerUsuarioUseCase {

    private final UsuarioRepository repository;

    public Mono<Usuario> obtenerUsuarioPorDocumentoId(Long documentoId){
        return repository.obtenerPorDocumentoId(documentoId);
    }

}
