package com.swissmedical.patients.infrastructure.adapter.in.rest.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResponse<T> {

    private boolean success;
    private String message;

    protected BaseResponse() {
    }

    protected BaseResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
