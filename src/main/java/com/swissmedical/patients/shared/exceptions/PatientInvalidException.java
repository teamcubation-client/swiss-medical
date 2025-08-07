package com.swissmedical.patients.shared.exceptions;

public class PatientInvalidException extends RuntimeException {

    public PatientInvalidException(String message) {
        super(message);
    }
}
