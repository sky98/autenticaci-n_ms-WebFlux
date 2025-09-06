package co.com.pragma.usecase.gestorsesion;

import co.com.pragma.model.usuario.errores.ErrorDominio;
import co.com.pragma.usecase.fabricas.UsuarioFabrica;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.GestorCredencialesPort;
import co.com.pragma.model.usuario.gateways.JwtUtilsPort;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private GestorCredencialesPort gestorCredencialesPort;

    @Mock
    private JwtUtilsPort jwtUtilsPort;

    @InjectMocks
    private GestorSesionUseCase gestorSesionUseCase;

    private Usuario usuarioBuilder = UsuarioFabrica.builder().build();

    @Test
    @DisplayName("Debe retornar un token si las credenciales son válidas")
    void login_shouldReturnToken_whenCredentialsAreValid() {

        String tokenEsperado = "mock_jwt_token";

        when(usuarioRepository.obtenerPorCorreo(any(String.class))).thenReturn(Mono.just(usuarioBuilder));
        when(gestorCredencialesPort.validarContrasena(any(String.class), any(Usuario.class))).thenReturn(Mono.just(true));
        when(jwtUtilsPort.generarToken(anyString(), anyLong(), anyLong())).thenReturn(Mono.just(tokenEsperado));

        StepVerifier.create(gestorSesionUseCase.Login("correo", "contrasena"))
                .expectNext(tokenEsperado)
                .verifyComplete();

        verify(usuarioRepository, times(1)).obtenerPorCorreo(anyString());
        verify(gestorCredencialesPort, times(1)).validarContrasena(anyString(), any(Usuario.class));
        verify(jwtUtilsPort, times(1)).generarToken(anyString(), anyLong(), anyLong());
    }

    @Test
    @DisplayName("Debe lanzar un error si el usuario no existe")
    void login_shouldThrowError_whenUserNotFound() {
        when(usuarioRepository.obtenerPorCorreo(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(gestorSesionUseCase.Login("correo", "contrasena"))
                .expectErrorMatches(throwable -> throwable instanceof ErrorDominio &&
                        throwable.getMessage().equals("Credenciales invalidas"))
                .verify();

        verify(gestorCredencialesPort, never()).validarContrasena(anyString(), any(Usuario.class));
        verify(jwtUtilsPort, never()).generarToken(anyString(), anyLong(), anyLong());
    }

    @Test
    @DisplayName("Debe lanzar un error si la contraseña es incorrecta")
    void login_shouldThrowError_whenPasswordIsInvalid() {

        when(usuarioRepository.obtenerPorCorreo(anyString())).thenReturn(Mono.just(usuarioBuilder));
        when(gestorCredencialesPort.validarContrasena(anyString(), any(Usuario.class))).thenReturn(Mono.just(false));

        StepVerifier.create(gestorSesionUseCase.Login("correo", "contrasena"))
                .expectErrorMatches(throwable -> throwable instanceof ErrorDominio &&
                        throwable.getMessage().equals("Credenciales invalidas"))
                .verify();

        verify(jwtUtilsPort, never()).generarToken(anyString(), anyLong(), anyLong());
    }

}
