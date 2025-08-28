package co.com.pragma.api;

import co.com.pragma.api.dto.request.CrearUsuarioDTO;
import co.com.pragma.api.dto.response.UsuarioDTO;
import co.com.pragma.api.mapper.UsuarioDTOMapper;
import co.com.pragma.api.router.ConsultarUsuarioRouter;
import co.com.pragma.api.router.CrearUsuarioRouter;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.UsuarioEstado;
import co.com.pragma.usecase.consultarusuario.ConsultarUsuarioUseCase;
import co.com.pragma.usecase.usuario.CrearUsuarioUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class, CrearUsuarioRouter.class, ConsultarUsuarioRouter.class})
@WebFluxTest
class RouterRestTest {

    private final String BASE_PATH = "/api/v1/usuarios";

    private final CrearUsuarioDTO crearUsuarioDTO = CrearUsuarioDTO.builder()
            .nombres("test nombre")
            .apellidos("test apellidos")
            .fechaNacimiento(LocalDate.of(1990, 1, 9))
            .direccion("test direccion")
            .telefono("555-5555")
            .correoElectronico("test@correo.com")
            .salarioBase(BigDecimal.valueOf(8500.00))
            .build();

    private final Usuario usuario = Usuario.builder()
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

    private final UsuarioDTO responseDTO = UsuarioDTO.builder()
            .usuarioId(1L)
            .nombres("test nombre")
            .apellidos("test apellidos")
            .fechaNacimiento(LocalDate.of(1990, 1, 9))
            .direccion("test direccion")
            .telefono("555-5555")
            .correoElectronico("test@correo.com")
            .salarioBase(BigDecimal.valueOf(8500.00))
            .estado("ACTIVO")
            .build();

    @MockitoBean
    private CrearUsuarioUseCase crearUsuarioUseCase;
    @MockitoBean
    private ConsultarUsuarioUseCase consultarUsuarioUseCase;
    @MockitoBean
    private UsuarioDTOMapper mapper;
    @MockitoBean
    private ValidadorRequest validador;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testListenPOSTUseCase() {

        when(mapper.toModel(any(CrearUsuarioDTO.class))).thenReturn(usuario);
        when(mapper.toResponse(any(Usuario.class))).thenReturn(responseDTO);

        when(validador.validar(any(CrearUsuarioDTO.class))).thenReturn(Mono.just(crearUsuarioDTO));

        when(crearUsuarioUseCase.guardar(any(Usuario.class))).thenReturn(Mono.just(usuario));


        webTestClient.post()
                .uri(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(crearUsuarioDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioDTO.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse.usuarioId()).isEqualTo(usuario.getUsuarioId());
                        }
                );
    }

    @Test
    void testListenGETExistsByDocumentoId_UsuarioNotExists() {
        when(consultarUsuarioUseCase.existeUsuarioPorDocumentoActivo(any(Long.class))).thenReturn(Mono.just(false));

        webTestClient.get()
                .uri(BASE_PATH + "/documento/{documentoId}/existe", 987654321L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .value(responseBody -> {
                    Assertions.assertThat(responseBody.get("existe")).isEqualTo(false);
                });
    }
}
