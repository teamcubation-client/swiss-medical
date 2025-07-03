package com.tq.pacientes.exceptions;

public class PacienteDuplicadoException extends RuntimeException {
    public PacienteDuplicadoException(String dni) {
        super("Ya existe un paciente con el DNI " + dni);
    }
}
