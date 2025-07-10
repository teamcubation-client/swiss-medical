package com.tq.pacientes.dtos;

public record PatientDTO(
        String firstName,
        String lastName,
        String dni,
        String healthInsurance,
        String healthPlan,
        String address,
        String phoneNumber,
        String email) {
}
