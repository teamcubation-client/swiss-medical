package microservice.pacientes.shared;

import java.time.LocalDate;

public class InvalidFechaAltaException extends RuntimeException {
    public static final String MESSAGE = "La fecha de alta no puede ser mayor a la fecha actual";
    public InvalidFechaAltaException(LocalDate message) {
        super(MESSAGE);
    }
}
