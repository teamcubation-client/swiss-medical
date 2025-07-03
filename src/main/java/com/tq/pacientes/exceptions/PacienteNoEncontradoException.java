package com.tq.pacientes.exceptions;

public class PacienteNoEncontradoException extends RuntimeException {
    public PacienteNoEncontradoException(Long id) {
        super("Paciente con ID " + id + " no encontrado.");
    }
}
