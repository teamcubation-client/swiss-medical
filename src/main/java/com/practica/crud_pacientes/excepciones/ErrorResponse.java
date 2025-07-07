package com.practica.crud_pacientes.excepciones;


import java.time.LocalDateTime;
import java.util.Map;


public class ErrorResponse {

    private LocalDateTime fechaHora;
    private int status;
    private String mensaje;
    private String error;
    private String path;
    private Map<String, String> errores;

    public ErrorResponse(LocalDateTime fechaHora, int status, String mensaje, String error, String path) {
        this.fechaHora = fechaHora;
        this.status = status;
        this.mensaje = mensaje;
        this.error = error;
        this.path = path;
    }

    public ErrorResponse(LocalDateTime fechaHora, int status, String mensaje, String error, String path, Map<String, String> errores) {
        this.fechaHora = fechaHora;
        this.status = status;
        this.mensaje = mensaje;
        this.error = error;
        this.path = path;
        this.errores = errores;
    }

    public Map<String, String> getErrores() {
        return errores;
    }

    public void setErrores(Map<String, String> errores) {
        this.errores = errores;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
