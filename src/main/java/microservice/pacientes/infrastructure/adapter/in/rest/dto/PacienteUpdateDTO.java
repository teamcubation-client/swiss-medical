package microservice.pacientes.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PacienteUpdateDTO {
    @Pattern(regexp = ".*\\S.*", message = "El nombre no puede ser vacío")
    private String nombre;
    @Pattern(regexp = ".*\\S.*", message = "El apellido no puede ser vacío")
    private String apellido;
    private String obraSocial;
    private String email;
    private String telefono;
}
