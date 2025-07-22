package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.mapper;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;

public class PatientRestMapper {

    public static Patient toDomain(PatientRequest patientRequest) {
        Patient patient = new Patient();
        patient.setName(patientRequest.getName());
        patient.setLastName(patientRequest.getLastName());
        patient.setDni(patientRequest.getDni());
        patient.setHealthInsuranceProvider(patientRequest.getHealthInsuranceProvider());
        patient.setPhoneNumber(patientRequest.getPhoneNumber());
        return patient;
    }

    public static PatientResponse toResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setName(patient.getName());
        response.setLastName(patient.getLastName());
        response.setDni(patient.getDni());
        return response;
    }
}
