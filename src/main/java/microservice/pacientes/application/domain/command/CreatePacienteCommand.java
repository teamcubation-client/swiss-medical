package microservice.pacientes.application.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePacienteCommand {
    private final String dni;
    private final String nombre;
    private final String apellido;
    private final String obra_social;
    private final String email;
    private final String telefono;
}
