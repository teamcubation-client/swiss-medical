package microservice.pacientes.application.domain.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePacienteCommand {
    private String nombre;
    private String apellido;
    private String obraSocial;
    private String email;
    private String telefono;
}
