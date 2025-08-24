package co.com.pragma.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table("usuarios")
public class UsuarioEntity {

    @Id
    @Column("usuario_id")
    private Long usuarioId;
    private String nombres;
    private String apellidos;
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    @Column("correo_electronico")
    private String correoElectronico;
    @Column("salario_base")
    private BigDecimal salarioBase;
    private String estado;

}
