package microservice.pacientes.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PacienteException extends RuntimeException {
    private String message;
    private int status;

    public PacienteException(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
