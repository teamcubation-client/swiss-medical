package com.tq.pacientes.shared.exceptions;

public class DuplicatePatientException extends RuntimeException {
    public DuplicatePatientException(String dni) {
        super("A patient with DNI " + dni + " already exists");
    }
}