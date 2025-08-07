package com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.mapper;

import com.swissmedical.patients.application.domain.factory.PatientFactory;
import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.entity.PatientEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PatientEntityMapper {

  private final PatientFactory patientFactory;

  public Patient toDomain(PatientEntity patientEntity) {
    return patientFactory.create(
            patientEntity.getId(),
            patientEntity.getFirstName(),
            patientEntity.getLastName(),
            patientEntity.getEmail(),
            patientEntity.getPhoneNumber(),
            patientEntity.getDni(),
            patientEntity.getMemberNumber(),
            patientEntity.getBirthDate(),
            patientEntity.isActive(),
            patientEntity.getSocialSecurity());
  }

  public PatientEntity toEntity(Patient patient) {
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
