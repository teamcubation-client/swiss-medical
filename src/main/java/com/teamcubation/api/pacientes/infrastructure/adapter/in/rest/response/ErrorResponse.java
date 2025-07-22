package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response;

public class ErrorResponse<T> extends ApiResponse<T> {

    private static final String DEFAULT_ERROR_MESSAGE = "Ocurrió un error inesperado";

    public ErrorResponse(String message, T data) {
        super(false, message, data);
    }

    public ErrorResponse(String message) {
        this(message, null);
    }

    public ErrorResponse() {
        super(false, DEFAULT_ERROR_MESSAGE, null);
    }
}
