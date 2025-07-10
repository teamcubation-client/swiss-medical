package com.teamcubation.api.pacientes.exception;

public class PacienteNoActualizadoException extends RuntimeException{
    public PacienteNoActualizadoException(Long id){
        super("No se pudo actualizar el paciente con id: " + id);
    }
}
