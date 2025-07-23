package microservice.pacientes.application.domain.port.in;

import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteDuplicadoException;

public interface CreatePacienteUseCase {
    Paciente create(CreatePacienteCommand createPacienteCommand) throws PacienteDuplicadoException;
}
