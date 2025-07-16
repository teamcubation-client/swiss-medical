package microservice.pacientes.application.domain.port.in;

import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteNoEncontradoException;

public interface UpdatePacienteUseCase {
    Paciente update(String dni, UpdatePacienteCommand updatePacienteCommand) throws PacienteNoEncontradoException;
}
