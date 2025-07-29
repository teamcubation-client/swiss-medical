package microservice.pacientes.application.domain.model;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    private String dni;
    private String nombre;
    private String apellido;
    private String obraSocial;
    private String email;
    private String telefono;
}
