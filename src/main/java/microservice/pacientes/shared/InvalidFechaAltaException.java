package microservice.pacientes.shared;

import java.time.LocalDate;

public class InvalidFechaAltaException extends RuntimeException {
    public InvalidFechaAltaException(LocalDate message) {
        super("La fecha de alta no puede ser mayor a la fecha actual");
    }
}
