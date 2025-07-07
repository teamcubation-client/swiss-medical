package microservice.pacientes.exception;

public class PacienteNoEncontradoException extends PacienteException {
    public PacienteNoEncontradoException() {
        super("Paciente no encontrado", 204);
    }
}
