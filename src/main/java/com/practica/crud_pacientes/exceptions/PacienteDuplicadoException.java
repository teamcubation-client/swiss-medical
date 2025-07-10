package com.practica.crud_pacientes.exceptions;

public class PacienteDuplicadoException extends RuntimeException{
    public PacienteDuplicadoException(){
        super("Ya existe un paciente con ese DNI.");
    }
}
