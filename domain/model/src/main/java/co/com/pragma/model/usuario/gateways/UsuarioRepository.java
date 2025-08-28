package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {
    Mono<Usuario> guardar(Usuario usuario);
    Mono<Boolean> existeCorreoElectronico(String correoElectronico);
    Mono<Boolean> existeUsuarioPorDocumentoActivo(Long documentoId);
    Mono<Boolean> existeUsuarioPorDocumento(Long documentoId);

}
