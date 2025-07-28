package microservice.pacientes.infrastructure.adapter.in.rest.advice;

public class ApiError {
    private String message;
    private int status;

    public ApiError(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String mensaje) {
        this.message = mensaje;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
