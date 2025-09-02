package co.com.pragma.usecase.usuario;

import co.com.pragma.usecase.fabricas.UsuarioFabrica;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.errores.ErrorDominio;
import co.com.pragma.model.usuario.gateways.GestorCredencialesPort;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository repository;
    @Mock
    private GestorCredencialesPort gestorCredencialesPort;

    @InjectMocks
    private CrearUsuarioUseCase useCase;

    private Usuario usuarioBuilder = UsuarioFabrica.builder().build();

    @Test
    void testGuardar_TodoCorrecto_DebeGuardar(){
        when(repository.existeUsuarioPorDocumento(any(Long.class))).thenReturn(Mono.just(false));
        when(repository.existeCorreoElectronico(any(String.class))).thenReturn(Mono.just(false));
        when(gestorCredencialesPort.encryptarContrasena(any(Usuario.class))).thenReturn(usuarioBuilder);
        when(repository.guardar(any(Usuario.class))).thenReturn(Mono.just(usuarioBuilder));

        Mono<Usuario> resultado = useCase.guardar(usuarioBuilder);

        StepVerifier.create(resultado)
                .expectNextMatches(u -> u.getCorreoElectronico().equals(usuarioBuilder.getCorreoElectronico()))
                .verifyComplete();

        verify(repository).existeCorreoElectronico(usuarioBuilder.getCorreoElectronico());
        verify(repository).guardar(usuarioBuilder);
    }

    @Test
    void testGuardar_CorreoYaExiste_DebeLanzarErrorDominio() {
        when(repository.existeUsuarioPorDocumento(any(Long.class))).thenReturn(Mono.just(false));
        when(repository.existeCorreoElectronico(any(String.class))).thenReturn(Mono.just(true));

        Mono<Usuario> resultado = useCase.guardar(usuarioBuilder);

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable ->
                        throwable instanceof ErrorDominio &&
                                throwable.getMessage().equals("Correo no está disponible") &&
                                ((ErrorDominio) throwable).getCampos().equals(Set.of("correoElectronico"))
                )
                .verify();

        verify(repository).existeCorreoElectronico(usuarioBuilder.getCorreoElectronico());
        verify(repository, never()).guardar(any(Usuario.class));
    }

    @Test
    void testGuardar_DocumentoIdYaExiste_DebeLanzarErrorDominio() {
        when(repository.existeUsuarioPorDocumento(any(Long.class))).thenReturn(Mono.just(true));

        Mono<Usuario> resultado = useCase.guardar(usuarioBuilder);

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable ->
                        throwable instanceof ErrorDominio &&
                                throwable.getMessage().equals("Documento ya está registrado") &&
                                ((ErrorDominio) throwable).getCampos().equals(Set.of("documentoId"))
                )
                .verify();

        verify(repository).existeUsuarioPorDocumento(usuarioBuilder.getDocumentoId());
        verify(repository, never()).guardar(any(Usuario.class));
    }

}
