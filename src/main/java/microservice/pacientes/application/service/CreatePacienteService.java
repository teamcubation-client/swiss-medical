package microservice.pacientes.application.service;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.mapper.CreatePacienteMapper;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.CreatePacienteUseCase;
import microservice.pacientes.application.domain.port.out.PacienteRepositoryPort;
import microservice.pacientes.shared.exception.PacienteDuplicadoException;

@AllArgsConstructor
public class CreatePacienteService implements CreatePacienteUseCase {

    private final PacienteRepositoryPort pacienteRepositoryPort;

    @Override
    public Paciente create(CreatePacienteCommand createPacienteCommand) throws PacienteDuplicadoException {
        Paciente paciente = CreatePacienteMapper.toEntity(createPacienteCommand);
        if (pacienteRepositoryPort.existsByDni(paciente.getDni()))
            throw new PacienteDuplicadoException();
        return pacienteRepositoryPort.save(paciente);
    }
}
