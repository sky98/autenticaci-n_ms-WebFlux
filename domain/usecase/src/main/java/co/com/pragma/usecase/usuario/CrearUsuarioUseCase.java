package co.com.pragma.usecase.usuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.errores.ErrorDominio;
import co.com.pragma.model.usuario.gateways.GestorCredencialesPort;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Set;

@RequiredArgsConstructor
public class CrearUsuarioUseCase {

    private final UsuarioRepository repository;
    private final GestorCredencialesPort gestorCredencialesPort;

    public Mono<Usuario> guardar(Usuario usuario){
        return validarExistencia(usuario)
                .map(gestorCredencialesPort::encryptarContrasena)
                .flatMap(repository::guardar);
    }

    private Mono<Usuario> validarExistencia(Usuario usuario) {
        return repository.existeUsuarioPorDocumento(usuario.getDocumentoId())
                .flatMap(existeDocumento -> existeDocumento
                        ? Mono.error(new ErrorDominio("Documento ya está registrado", Set.of("documentoId")))
                        : repository.existeCorreoElectronico(usuario.getCorreoElectronico()))
                .flatMap(existeCorreo -> existeCorreo
                        ? Mono.error(new ErrorDominio("Correo no está disponible", Set.of("correoElectronico")))
                        : Mono.just(usuario));
    }

}
