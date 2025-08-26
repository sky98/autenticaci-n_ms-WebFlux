package co.com.pragma.api.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CrearUsuarioDTO(
        @NotBlank(message = "El campo :nombres es obligatorio")
        String nombres,
        @NotBlank(message = "El campo :documentoId es obligatorio")
        @Digits(integer = 12, fraction = 0, message = "El campo :documentoId debe ser un número entero sin decimales y con un máximo de 12 dígitos")
        @Size(min = 7, max = 12, message = "El campo :documentoId debe tener entre 7 y 12 caracteres")
        String documentoId,
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
