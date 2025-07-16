package com.practica.crud_pacientes.shared.exceptions;

public class PacienteNoEncontradoException extends RuntimeException{
    public PacienteNoEncontradoException(){
        super("El paciente que se intenta buscar no existe.");
    }
}
