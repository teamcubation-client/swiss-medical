package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response;

public class SuccessResponse<T> extends ApiResponse<T> {

    private static final String DEFAULT_SUCCESS_MESSAGE = "Operación exitosa";

    public SuccessResponse(String message, T data) {
        super(true, message, data);
    }

    public SuccessResponse(T data) {
        super(true, DEFAULT_SUCCESS_MESSAGE, data);
    }
}
