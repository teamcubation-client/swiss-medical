package com.tq.pacientes.shared.exceptions;

public class PatientDniNotFoundException extends RuntimeException {
    public PatientDniNotFoundException(String dni) {
        super("Patient with DNI " + dni + " not found.");
    }
}