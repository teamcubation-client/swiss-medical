package com.swissmedical.patients.infrastructure.adapter.in.rest.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ApiResponse<T> {

    private boolean success;
    private String message;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
