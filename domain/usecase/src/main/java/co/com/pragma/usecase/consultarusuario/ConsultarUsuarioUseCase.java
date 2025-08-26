package co.com.pragma.usecase.consultarusuario;

import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ConsultarUsuarioUseCase {

    private final UsuarioRepository repository;

    public Mono<Boolean> existeUsuarioPorDocumentoActivo(Long documentoId){
        return repository.existeUsuarioPorDocumentoActivo(documentoId);
    }
}
