package microservice.pacientes.application.service;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.DeletePacienteUseCase;
import microservice.pacientes.application.domain.port.out.PacienteRepositoryPort;
import microservice.pacientes.shared.exception.PacienteNoEncontradoException;

@AllArgsConstructor
public class DeletePacienteService implements DeletePacienteUseCase {

    private final PacienteRepositoryPort pacienteRepositoryPort;

    @Override
    public boolean delete(String dni) throws PacienteNoEncontradoException {
        Paciente paciente = pacienteRepositoryPort.getByDni(dni)
                .orElseThrow(PacienteNoEncontradoException::new);
        pacienteRepositoryPort.delete(paciente);
        return true;
    }
}
