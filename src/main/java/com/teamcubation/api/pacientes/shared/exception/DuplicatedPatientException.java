package com.teamcubation.api.pacientes.shared.exception;

public class DuplicatedPatientException extends RuntimeException {
    public DuplicatedPatientException(String campo, String valor) {
        super(String.format("Ya existe un paciente con %s: %s", campo, valor));
    }
}