package swissmedical.exception;

/**
 * Excepcion cuando no se encuentra un paciente con el ID especificado
 */
public class PacienteNotFoundException extends RuntimeException {
    /**
     * Nueva excepcion indicando el ID no fue encontrado
     * @param id identificador del paciente no encontrado
     */
    public PacienteNotFoundException(Long id) {
        super("Paciente no encontrado con id: " + id);
    }
} 