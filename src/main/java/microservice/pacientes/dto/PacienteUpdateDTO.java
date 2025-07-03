package microservice.pacientes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacienteUpdateDTO {
    private String nombre;
    private String apellido;
    private String obraSocial;
    private String email;
    private String telefono;
}
