package microservice.pacientes.exception;

public class PacienteDuplicadoException extends PacienteException {
    public PacienteDuplicadoException() {
        super("Paciente duplicado", 409);
    }
}
