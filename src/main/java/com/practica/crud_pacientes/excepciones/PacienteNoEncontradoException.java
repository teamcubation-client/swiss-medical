package com.practica.crud_pacientes.excepciones;

public class PacienteNoEncontradoException extends RuntimeException{
    public PacienteNoEncontradoException(){
        super("El paciente que se intenta buscar no existe.");
    }
}
