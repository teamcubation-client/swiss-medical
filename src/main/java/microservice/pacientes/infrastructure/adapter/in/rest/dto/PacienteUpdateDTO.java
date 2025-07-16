package microservice.pacientes.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacienteUpdateDTO {
    @Pattern(regexp = ".*\\S.*", message = "El nombre no puede ser vacío")
    private String nombre;
    @Pattern(regexp = ".*\\S.*", message = "El apellido no puede ser vacío")
    private String apellido;
    private String obraSocial;
    private String email;
    private String telefono;
}
