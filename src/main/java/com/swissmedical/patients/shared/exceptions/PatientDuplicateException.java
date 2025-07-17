package com.swissmedical.patients.shared.exceptions;

public class PatientDuplicateException extends RuntimeException {

    public PatientDuplicateException(String message) {
        super(message);
    }
}
