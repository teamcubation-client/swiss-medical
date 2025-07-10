package com.teamcubation.api.pacientes.exception;

public class PacienteDuplicadoException  extends RuntimeException {
    public PacienteDuplicadoException(String campo, String valor) {
        super(String.format("Ya existe un paciente con %s: %s", campo, valor));
    }
}