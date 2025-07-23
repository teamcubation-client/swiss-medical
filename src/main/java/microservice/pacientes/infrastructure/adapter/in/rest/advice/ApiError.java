package microservice.pacientes.infrastructure.adapter.in.rest.advice;

public class ApiError {
    private String mensaje;
    private int status;

    public ApiError(String mensaje, int status) {
        this.mensaje = mensaje;
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
