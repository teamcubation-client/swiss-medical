package com.swissmedical.patients.infrastructure.adapter.in.rest.response;

public class ErrorResponse<T> extends ApiResponse<T> {
    public ErrorResponse(String message) {
        super(false, message);
    }
}
