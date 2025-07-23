package microservice.pacientes.shared;

public class PacienteNullException extends RuntimeException {
    public PacienteNullException() {
        super("No se pudo mapear, Paciente no puede ser null");
    }
}
