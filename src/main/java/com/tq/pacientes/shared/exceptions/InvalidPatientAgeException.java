package com.tq.pacientes.shared.exceptions;

public class InvalidPatientAgeException extends RuntimeException {
    public InvalidPatientAgeException(int age) {
        super("Invalid patient age: " + age);
    }
}
