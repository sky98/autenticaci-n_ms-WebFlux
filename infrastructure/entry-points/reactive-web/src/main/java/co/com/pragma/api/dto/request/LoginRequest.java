package co.com.pragma.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El campo :correoElectronico es obligatorio")
        @Email(message = "Formato invalido del campo :correoElectronico")
        String correoElectronico,
        @NotBlank(message = "El campo :contrasena es obligatorio")
        String contrasena
) {
}
