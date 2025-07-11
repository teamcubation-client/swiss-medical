package com.tq.pacientes.exceptions;

public class DuplicatePatientException extends RuntimeException {
    public DuplicatePatientException(String dni) {
        super("A patient with DNI " + dni + " already exists");
    }
}