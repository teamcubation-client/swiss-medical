package microservice.pacientes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PacienteResponseDTO {
    private String dni;
    private String nombre;
    private String apellido;
    private String obraSocial;
}
