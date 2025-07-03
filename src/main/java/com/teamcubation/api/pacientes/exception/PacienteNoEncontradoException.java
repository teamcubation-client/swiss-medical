package com.teamcubation.api.pacientes.exception;

public class PacienteNoEncontradoException extends RuntimeException {
        public PacienteNoEncontradoException(Long id) {
            super("Paciente con ID: " + id + " no encontrado");
        }

}
