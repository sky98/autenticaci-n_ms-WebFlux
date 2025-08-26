package co.com.pragma.usecase.usuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.UsuarioEstado;
import co.com.pragma.model.usuario.errores.ErrorDominio;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private CrearUsuarioUseCase useCase;

    private Usuario usuario = Usuario.builder()
            .usuarioId(1L)
            .nombres("test nombre")
            .apellidos("test apellidos")
            .fechaNacimiento(LocalDate.of(1990, 1, 9))
            .direccion("test direccion")
            .telefono("555-5555")
            .correoElectronico("test@correo.com")
            .salarioBase(BigDecimal.valueOf(8500.00))
            .estado(UsuarioEstado.ACTIVO)
            .build();

    @Test
    void testGuardar_TodoCorrecto_DebeGuardar(){
        when(repository.existeCorreoElectronico(any(String.class))).thenReturn(Mono.just(false));
        when(repository.guardar(any(Usuario.class))).thenReturn(Mono.just(usuario));

        Mono<Usuario> resultado = useCase.guardar(usuario);

        StepVerifier.create(resultado)
                .expectNextMatches(u -> u.getCorreoElectronico().equals(usuario.getCorreoElectronico()))
                .verifyComplete();

        verify(repository).existeCorreoElectronico(usuario.getCorreoElectronico());
        verify(repository).guardar(usuario);
    }

    @Test
    void testGuardar_CorreoYaExiste_DebeLanzarErrorDominio() {
        when(repository.existeCorreoElectronico(any(String.class))).thenReturn(Mono.just(true));

        Mono<Usuario> resultado = useCase.guardar(usuario);

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable ->
                        throwable instanceof ErrorDominio &&
                                throwable.getMessage().equals("Correo no esta disponible") &&
                                ((ErrorDominio) throwable).getCampos().equals(Set.of("correoElectronico"))
                )
                .verify();

        verify(repository).existeCorreoElectronico(usuario.getCorreoElectronico());
        verify(repository, never()).guardar(any(Usuario.class));
    }

}
