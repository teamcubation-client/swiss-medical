package microservice.pacientes.exception;

import org.springframework.http.HttpStatus;

public class PacienteDuplicadoException extends PacienteException {
    public PacienteDuplicadoException() {
        super("Paciente duplicado", HttpStatus.CONFLICT);
    }
}
