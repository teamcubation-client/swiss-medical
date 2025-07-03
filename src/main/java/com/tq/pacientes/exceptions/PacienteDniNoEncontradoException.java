package com.tq.pacientes.exceptions;

public class PacienteDniNoEncontradoException extends RuntimeException {
    public PacienteDniNoEncontradoException(String dni) {
        super("Paciente con DNI " + dni + " no encontrado.");
    }
}
