package microservice.pacientes.exception;

public class PacienteNoEncontradoException extends PacienteException {
    public PacienteNoEncontradoException(String message, int status) {
        super(message, status);
    }
}
