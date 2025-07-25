package microservice.pacientes.application.domain.port.out;

import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;

public interface PacienteUpdater {
    void update(UpdatePacienteCommand command, Paciente paciente);
}
