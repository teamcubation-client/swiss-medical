package com.tq.pacientes.mappers;

import com.tq.pacientes.dtos.PatientDTO;
import com.tq.pacientes.models.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    public PatientDTO toDto(Patient patient) {
        return new PatientDTO(
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDni(),
                patient.getHealthInsurance(),
                patient.getHealthPlan(),
                patient.getAddress(),
                patient.getPhoneNumber(),
                patient.getEmail());
    }

    public Patient toEntity(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setFirstName(dto.firstName());
        patient.setLastName(dto.lastName());
        patient.setDni(dto.dni());
        patient.setHealthInsurance(dto.healthInsurance());
        patient.setHealthPlan(dto.healthPlan());
        patient.setAddress(dto.address());
        patient.setPhoneNumber(dto.phoneNumber());
        patient.setEmail(dto.email());
        return patient;
    }
}
