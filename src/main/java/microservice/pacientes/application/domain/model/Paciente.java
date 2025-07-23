package microservice.pacientes.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Paciente {
    private String dni;
    private String nombre;
    private String apellido;
    private String obraSocial;
    private String email;
    private String telefono;
}
