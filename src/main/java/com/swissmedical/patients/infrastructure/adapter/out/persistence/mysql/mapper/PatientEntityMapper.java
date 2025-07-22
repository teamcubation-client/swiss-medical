package com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.entity.PatientEntity;

public class PatientEntityMapper {

  public static Patient toDomain(PatientEntity patientEntity) {
    return new Patient.PatientBuilder()
            .id(patientEntity.getId())
            .firstName(patientEntity.getFirstName())
            .lastName(patientEntity.getLastName())
            .email(patientEntity.getEmail())
            .phoneNumber(patientEntity.getPhoneNumber())
            .dni(patientEntity.getDni())
            .memberNumber(patientEntity.getMemberNumber())
            .birthDate(patientEntity.getBirthDate())
            .isActive(patientEntity.isActive())
            .socialSecurity(patientEntity.getSocialSecurity())
            .build();
  }

  public static PatientEntity toEntity(Patient patient) {
    return PatientEntity.builder()
            .id(patient.getId())
            .firstName(patient.getFirstName())
            .lastName(patient.getLastName())
            .email(patient.getEmail())
            .phoneNumber(patient.getPhoneNumber())
            .dni(patient.getDni())
            .memberNumber(patient.getMemberNumber())
            .birthDate(patient.getBirthDate())
            .isActive(patient.isActive())
            .socialSecurity(patient.getSocialSecurity())
            .build();
  }
}
