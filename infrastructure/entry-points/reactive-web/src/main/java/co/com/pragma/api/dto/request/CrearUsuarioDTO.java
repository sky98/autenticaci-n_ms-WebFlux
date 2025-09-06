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
        @NotNull(message = "El campo :fechaNacimiento es obligatorio")
        LocalDate fechaNacimiento,
        String direccion,
        String telefono,
        @NotBlank(message = "El campo :correoElectronico es obligatorio")
        @Email(message = "Formato invalido del campo :correoElectronico")
        String correoElectronico,
        @NotNull(message = "El campo :salarioBase es obligatorio")
        @DecimalMin(value = "0", inclusive = false, message = "El campo :salariBase debe ser mayor a 0")
        @DecimalMax(value = "15000000", message = "El campo :salarioBase no puede superar 15,000,000")
        BigDecimal salarioBase,
        @NotBlank(message = "El campo :contrasena es obligatorio")
        @Size(min = 7, message = "El campo :contrasena debe tener minimo 6 caracteres")
        String contrasena
) {
        @Override
        public String toString() {
                return "CrearUsuarioDTO{" +
                        "nombres='" + nombres + '\'' +
                        ", documentoId='" + documentoId + '\'' +
                        ", apellidos='" + apellidos + '\'' +
                        ", fechaNacimiento=" + fechaNacimiento +
                        ", direccion='" + direccion + '\'' +
                        ", telefono='" + telefono + '\'' +
                        ", correoElectronico='" + correoElectronico + '\'' +
                        ", salarioBase=" + salarioBase +
                        ", contrasena='****'" +
                        '}';
        }
}
