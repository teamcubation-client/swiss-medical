package com.teamcubation.api.pacientes.shared.exception;

public class PatientNotFoundException extends RuntimeException {
        public PatientNotFoundException(Long id) {
            super("Paciente con ID: " + id + " no encontrado");
        }

        public PatientNotFoundException(String dni) {
            super("Paciente con DNI: " + dni + " no encontrado");
        }
}