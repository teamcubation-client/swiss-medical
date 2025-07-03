package swissmedical.exception;

/**
 * Excepcion cuando se intenta registrar un paciente con un DNI existente
 */
public class PacienteDuplicadoException extends RuntimeException {
    /**
     * Nueva excepcion indicando el DNI esta duplicado
     * @param dni Documento Nacional de Identidad duplicado
     */
    public PacienteDuplicadoException(String dni) {
        super("Ya existe un paciente con el DNI: " + dni);
    }
} 