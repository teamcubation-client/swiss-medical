package com.teamcubation.api.pacientes.shared.exception;

public class PatientDniAlreadyInUseException extends RuntimeException{
    public PatientDniAlreadyInUseException(long id){
        super("El DNI ingresado ya se encuentra en uso en paciente con id: " + id);
    }
}
