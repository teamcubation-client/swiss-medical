package com.tq.pacientes.infrastructure.adapter.out.persistence.mapper;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.infrastructure.adapter.out.persistence.PatientEntity;

public class PatientPersistenceMapper {

    private PatientPersistenceMapper() {}

    public static Patient toDomain(PatientEntity entity) {
        return Patient.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .dni(entity.getDni())
                .birthDate(entity.getBirthDate())
                .healthInsurance(entity.getHealthInsurance())
                .healthPlan(entity.getHealthPlan())
                .address(entity.getAddress())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .active(entity.getActive())
                .creationDate(entity.getCreationDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }

    public static PatientEntity toEntity(Patient patient) {
        return PatientEntity.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dni(patient.getDni())
                .birthDate(patient.getBirthDate())
                .healthInsurance(patient.getHealthInsurance())
                .healthPlan(patient.getHealthPlan())
                .address(patient.getAddress())
                .phoneNumber(patient.getPhoneNumber())
                .email(patient.getEmail())
                .active(patient.getActive())
                .creationDate(patient.getCreationDate())
                .lastModifiedDate(patient.getLastModifiedDate())
                .build();
    }
} 