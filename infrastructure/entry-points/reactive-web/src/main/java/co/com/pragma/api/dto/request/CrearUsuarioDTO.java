package co.com.pragma.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CrearUsuarioDTO(
        @NotBlank(message = "El campo :nombres es obligatorio")
        String nombres,
        @NotBlank(message = "El campo :apellidos es obligatorio")
        String apellidos,
        @NotNull(message = "El campo :fecha_nacimiento es obligatorio")
        @JsonProperty("fecha_nacimiento")
        LocalDate fechaNacimiento,
        String direccion,
        String telefono,
        @NotBlank(message = "El campo :correo_electronico es obligatorio")
        @Email(message = "Formato invalido del campo :correo_electronico")
        @JsonProperty("correo_electronico")
        String correoElectronico,
        @NotNull(message = "El campo :salario_base es obligatorio")
        @DecimalMin(value = "0", inclusive = false, message = "El campo :salari_base debe ser mayor a 0")
        @DecimalMax(value = "15000000", message = "El campo :salario_base no puede superar 15,000,000")
        @JsonProperty("salario_base")
        BigDecimal salarioBase
) {}
