package com.tq.pacientes.shared.exceptions;

public class PatientAlreadyActiveException extends RuntimeException {
    public PatientAlreadyActiveException(Long id) {
        super("Patient with ID " + id + " is already active");
    }
} 