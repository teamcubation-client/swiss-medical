package microservice.pacientes.shared;

/**
 * Excepcion cuando no se encuentra un paciente con el ID especificado
 */
public class PacienteNotFoundException extends RuntimeException {
    private PacienteNotFoundException(String message) {
        super(message);
    }

    public static PacienteNotFoundException porId(Long id) {
        return new PacienteNotFoundException("Paciente no encontrado con id: " + id);
    }

    public static PacienteNotFoundException porDni(String dni) {
        return new PacienteNotFoundException("Paciente no encontrado con DNI: " + dni);
    }

    public static PacienteNotFoundException porNombre(String nombre) {
        return new PacienteNotFoundException("Paciente no encontrado con el Nombre: " + nombre);
    }

    public static PacienteNotFoundException porObraSocial(String obrasocial) {
        return new PacienteNotFoundException("Paciente no encontrado con la Obra Social: " + obrasocial);
    }
} 