package co.com.pragma.api.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CrearUsuarioDTO(
        @NotBlank(message = "El campo :nombres es obligatorio")
        String nombres,
        @NotBlank(message = "El campo :apellidos es obligatorio")
        String apellidos,
        @NotNull(message = "El campo :fecha_nacimiento es obligatorio")
        LocalDate fechaNacimiento,
        String direccion,
        String telefono,
        @NotBlank(message = "El campo :correo_electronico es obligatorio")
        @Email(message = "Formato invalido del campo :correo_electronico")
        String correoElectronico,
        @NotNull(message = "El campo :salario_base es obligatorio")
        @DecimalMin(value = "0", inclusive = false, message = "El campo :salari_base debe ser mayor a 0")
        @DecimalMax(value = "15000000", message = "El campo :salario_base no puede superar 15,000,000")
        BigDecimal salarioBase
) {}
