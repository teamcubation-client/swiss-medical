package microservice.pacientes.exception;

import org.springframework.http.HttpStatus;

public class PacienteNoEncontradoException extends PacienteException {
    public PacienteNoEncontradoException() {
        super("Paciente no encontrado", HttpStatus.NOT_FOUND);
    }
}
