package microservice.pacientes.infrastructure.adapter.out.persistence.mysql.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "pacientes")
public class PacienteEntity {
    @Id
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    private String obraSocial;
    private String email;
    private String telefono;
}
