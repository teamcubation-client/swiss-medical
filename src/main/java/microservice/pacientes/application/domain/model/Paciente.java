package microservice.pacientes.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Paciente {
    private String dni;
    private String nombre;
    private String apellido;
    private String obra_social;
    private String email;
    private String telefono;
}
