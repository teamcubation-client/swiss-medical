package microservice.pacientes.application.service;

import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.UpdatePacienteUseCase;
import microservice.pacientes.application.domain.port.out.PacienteRepositoryPort;
import microservice.pacientes.shared.exception.PacienteNoEncontradoException;

public class UpdatePacienteUseCaseImpl implements UpdatePacienteUseCase {

    private final PacienteRepositoryPort pacienteRepositoryPort;

    public UpdatePacienteUseCaseImpl(PacienteRepositoryPort pacienteRepositoryPort) {
        this.pacienteRepositoryPort = pacienteRepositoryPort;
    }

    @Override
    public Paciente update(String dni, UpdatePacienteCommand updatePacienteCommand) throws PacienteNoEncontradoException {
        Paciente paciente = pacienteRepositoryPort.getByDni(dni)
                .orElseThrow(PacienteNoEncontradoException::new);

        if (updatePacienteCommand.getNombre() != null) paciente.setNombre(updatePacienteCommand.getNombre());
        if (updatePacienteCommand.getApellido() != null) paciente.setApellido(updatePacienteCommand.getApellido());
        if (updatePacienteCommand.getObra_social() != null) paciente.setObra_social(updatePacienteCommand.getObra_social());
        if (updatePacienteCommand.getEmail() != null) paciente.setEmail(updatePacienteCommand.getEmail());
        if (updatePacienteCommand.getTelefono() != null) paciente.setTelefono(updatePacienteCommand.getTelefono());

        return pacienteRepositoryPort.save(paciente);
    }
}
