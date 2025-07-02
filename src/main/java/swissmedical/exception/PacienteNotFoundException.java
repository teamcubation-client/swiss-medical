package swissmedical.exception;

public class PacienteNotFoundException extends RuntimeException {
    public PacienteNotFoundException(Long id) {
        super("Paciente no encontrado con id: " + id);
    }
} 