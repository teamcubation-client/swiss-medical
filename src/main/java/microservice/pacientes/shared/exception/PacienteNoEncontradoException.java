package microservice.pacientes.shared.exception;

public class PacienteNoEncontradoException extends PacienteException {
    public PacienteNoEncontradoException() {
        super("Paciente no encontrado");
    }
}
