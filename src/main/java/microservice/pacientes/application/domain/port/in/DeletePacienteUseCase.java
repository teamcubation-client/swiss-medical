package microservice.pacientes.application.domain.port.in;

import microservice.pacientes.shared.exception.PacienteNoEncontradoException;

public interface DeletePacienteUseCase {
    void delete(String dni) throws PacienteNoEncontradoException;
}
