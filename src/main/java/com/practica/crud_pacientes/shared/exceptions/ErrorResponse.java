package com.practica.crud_pacientes.shared.exceptions;


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

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getError() {
        return error;
    }
}
