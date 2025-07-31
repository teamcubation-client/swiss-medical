package microservice.pacientes.shared.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PacienteException extends RuntimeException {
    private final String message;

    protected PacienteException(String message) {
        this.message = message;
    }
}
