package co.com.pragma.api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CrearUsuarioDTO(
     String nombres,
     String apellidos,
     LocalDate fechaNacimiento,
     String direccion,
     String telefono,
     String correoElectronico,
     BigDecimal salarioBase,
     String estado
) {}
