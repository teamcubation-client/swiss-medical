package com.teamcubation.api.pacientes.shared.exception;

public class PatientDniAlreadyInUse extends RuntimeException{
    public PatientDniAlreadyInUse(long id){
        super("El DNI ingresado ya se encuentra en uso en paciente con id: " + id);
    }
}
