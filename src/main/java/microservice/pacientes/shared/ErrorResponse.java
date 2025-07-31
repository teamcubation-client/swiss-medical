package microservice.pacientes.shared;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final int status;
    private final String mensaje;
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String mensaje, LocalDateTime timestamp) {
        this.status = status;
        this.mensaje = mensaje;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}