package microservice.pacientes.shared;

public class PacienteActivoException extends RuntimeException {
    public PacienteActivoException(Long id) {
        super("No se puede eliminar el paciente " + id + " porque esta activo");
    }
}
