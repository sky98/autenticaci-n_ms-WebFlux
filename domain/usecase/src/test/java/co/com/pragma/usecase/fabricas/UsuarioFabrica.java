package co.com.pragma.usecase.fabricas;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.UsuarioEstado;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UsuarioFabrica {

    private Usuario.UsuarioBuilder builder;

    public UsuarioFabrica(){
        this.builder = Usuario.builder()
                .usuarioId(1L)
                .documentoId(1L)
                .nombres("test nombre")
                .apellidos("test apellidos")
                .fechaNacimiento(LocalDate.of(1990, 1, 9))
                .direccion("test direccion")
                .telefono("555-5555")
                .correoElectronico("test@correo.com")
                .salarioBase(BigDecimal.valueOf(8500.00))
                .estado(UsuarioEstado.ACTIVO)
                .rolId(1L);
    }

    public static UsuarioFabrica builder(){
        return new UsuarioFabrica();
    }

    public Usuario.UsuarioBuilder with(){
        return this.builder;
    }

    public Usuario build(){
        return this.builder.build();
    }

}
