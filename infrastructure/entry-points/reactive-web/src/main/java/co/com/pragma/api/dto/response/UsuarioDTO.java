package co.com.pragma.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UsuarioDTO(
        Long usuarioId,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String direccion,
        String telefono,
        String correoElectronico,
        BigDecimal salarioBase,
        String estado
) {}
