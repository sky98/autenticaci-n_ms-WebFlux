package co.com.pragma.usecase.consultarusuario;

import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsultarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private ConsultarUsuarioUseCase useCase;

    private static final Long DOCUMENTO_ID = 123456789L;


    @Test
    void testExisteUsuarioPorDocumentoActivo_UsuarioExiste_DebeRetornarTrue() {
        when(repository.existeUsuarioPorDocumentoActivo(any(Long.class))).thenReturn(Mono.just(true));

        Mono<Boolean> resultado = useCase.existeUsuarioPorDocumentoActivo(DOCUMENTO_ID);

        StepVerifier.create(resultado)
                .expectNext(true)
                .verifyComplete();
        verify(repository, times(1)).existeUsuarioPorDocumentoActivo(DOCUMENTO_ID);
    }

    @Test
    void testExisteUsuarioPorDocumentoActivo_UsuarioNoExiste_DebeRetornarFalse() {
        when(repository.existeUsuarioPorDocumentoActivo(any(Long.class))).thenReturn(Mono.just(false));

        Mono<Boolean> resultado = useCase.existeUsuarioPorDocumentoActivo(DOCUMENTO_ID);

        StepVerifier.create(resultado)
                .expectNext(false)
                .verifyComplete();

        verify(repository, times(1)).existeUsuarioPorDocumentoActivo(DOCUMENTO_ID);
    }

    @Test
    void testExisteUsuarioPorDocumentoActivo_ErrorEnRepositorio_DebeLanzarError() {
        when(repository.existeUsuarioPorDocumentoActivo(any(Long.class)))
                .thenReturn(Mono.error(new RuntimeException("Error al consultar en la tabla usuarios")));

        Mono<Boolean> resultado = useCase.existeUsuarioPorDocumentoActivo(DOCUMENTO_ID);

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Error al consultar en la tabla usuarios")
                )
                .verify();

        verify(repository, times(1)).existeUsuarioPorDocumentoActivo(DOCUMENTO_ID);
    }

}
