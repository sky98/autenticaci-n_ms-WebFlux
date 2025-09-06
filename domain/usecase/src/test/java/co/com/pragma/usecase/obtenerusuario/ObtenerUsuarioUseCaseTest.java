package co.com.pragma.usecase.obtenerusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.fabricas.UsuarioFabrica;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObtenerUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private ObtenerUsuarioUseCase useCase;

    private Usuario usuarioBuilder = UsuarioFabrica.builder().build();
    private static final Long DOCUMENTO_ID = 1L;

    @Test
    void obtenerUsuarioPorDocumentoId_debeRetornarUsuario_cuandoExiste() {
        when(repository.obtenerPorDocumentoId(anyLong())).thenReturn(Mono.just(usuarioBuilder));

        Mono<Usuario> resultado = useCase.obtenerUsuarioPorDocumentoId(DOCUMENTO_ID);

        StepVerifier.create(resultado)
                .expectNext(usuarioBuilder)
                .verifyComplete();

        verify(repository, times(1)).obtenerPorDocumentoId(DOCUMENTO_ID);
    }

    @Test
    void obtenerUsuarioPorDocumentoId_debeRetornarMonoVacio_cuandoNoExiste() {
        when(repository.obtenerPorDocumentoId(anyLong())).thenReturn(Mono.empty());

        Mono<Usuario> resultado = useCase.obtenerUsuarioPorDocumentoId(DOCUMENTO_ID);

        StepVerifier.create(resultado)
                .expectNextCount(0)
                .verifyComplete();

        verify(repository, times(1)).obtenerPorDocumentoId(DOCUMENTO_ID);
    }

}
