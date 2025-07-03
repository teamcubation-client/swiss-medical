package microservice.pacientes.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PacienteException extends RuntimeException {
    private String message;

    public PacienteException(String message) {
        this.message = message;
    }
}
