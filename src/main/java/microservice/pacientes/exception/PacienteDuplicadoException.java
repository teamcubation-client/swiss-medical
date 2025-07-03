package microservice.pacientes.exception;

public class PacienteDuplicadoException extends PacienteException {
    public PacienteDuplicadoException(String message) {
        super(message);
    }
}
