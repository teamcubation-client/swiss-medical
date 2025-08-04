package microservice.pacientes.application.domain.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UpdatePacienteCommand {
    private final String nombre;
    private final String apellido;
    private final String obraSocial;
    private final String email;
    private final String telefono;
}
