package co.com.pragma.api.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record UsuarioDTO(
        Long usuarioId,
        Long documentoId,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String direccion,
        String telefono,
        String correoElectronico,
        BigDecimal salarioBase,
        String estado,
        Long rolId
) {}
