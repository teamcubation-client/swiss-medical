package com.swissmedical.patients.infrastructure.adapter.in.rest.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse<T> extends ApiResponse<T> {

    private static final String DEFAULT_SUCCESS_MESSAGE = "Operación exitosa";
    private T data;

    public SuccessResponse(T data) {
        super(true, DEFAULT_SUCCESS_MESSAGE);
        this.data = data;
    }

}
