package com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.mapper;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.entity.PatientEntity;

public class PatientPersistenceMapper {

    public static PatientEntity toEntity(Patient patient){
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setId(patient.getId());
        patientEntity.setName(patient.getName());
        patientEntity.setLastName(patient.getLastName());
        patientEntity.setDni(patient.getDni());
        patientEntity.setHealthInsuranceProvider(patient.getHealthInsuranceProvider());
        patientEntity.setEmail(patient.getEmail());
        patientEntity.setPhoneNumber(patient.getPhoneNumber());
        return patientEntity;
    }

    public static Patient toDomain(PatientEntity patientEntity) {
        Patient patient = new Patient();
        patient.setId(patientEntity.getId());
        patient.setName(patientEntity.getName());
        patient.setLastName(patientEntity.getLastName());
        patient.setDni(patientEntity.getDni());
        patient.setHealthInsuranceProvider(patientEntity.getHealthInsuranceProvider());
        patient.setEmail(patientEntity.getEmail());
        patient.setPhoneNumber(patientEntity.getPhoneNumber());
        return patient;
    }
}
