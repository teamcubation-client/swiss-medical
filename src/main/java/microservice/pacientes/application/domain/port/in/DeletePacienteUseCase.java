package microservice.pacientes.application.domain.port.in;

import microservice.pacientes.shared.exception.PacienteNoEncontradoException;

public interface DeletePacienteUseCase {
    boolean delete(String dni) throws PacienteNoEncontradoException;
}
