package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response;

public class SuccessResponse<T> extends ApiResponse<T> {

    private static final String DEFAULT_SUCCESS_MESSAGE = "Operación exitosa";
    private T data;

    public SuccessResponse(T data) {
        super(true, DEFAULT_SUCCESS_MESSAGE);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
