package com.tq.pacientes.shared.exceptions;

public class MissingBirthDateException extends RuntimeException {
    public MissingBirthDateException() {
        super("Birth date is required to classify the patient");
    }
}
