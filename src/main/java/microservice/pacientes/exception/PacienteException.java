package microservice.pacientes.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public abstract class PacienteException extends RuntimeException {
    private String message;
    private HttpStatus status;

    public PacienteException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
