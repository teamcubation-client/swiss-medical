package microservice.pacientes.shared.exception;

public class PacienteDuplicadoException extends PacienteException {
    public PacienteDuplicadoException() {
        super("Paciente duplicado");
    }
}
