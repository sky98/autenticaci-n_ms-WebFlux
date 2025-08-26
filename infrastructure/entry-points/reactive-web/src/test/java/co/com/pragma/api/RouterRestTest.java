package co.com.pragma.api;

import co.com.pragma.api.dto.request.CrearUsuarioDTO;
import co.com.pragma.api.dto.response.UsuarioDTO;
import co.com.pragma.api.mapper.UsuarioDTOMapper;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.UsuarioEstado;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

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
                .uri("/api/v1/usuarios")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(crearUsuarioDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioDTO.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse.usuarioId()).isEqualTo(usuario.getUsuarioId());
                        }
                );
    }
}
