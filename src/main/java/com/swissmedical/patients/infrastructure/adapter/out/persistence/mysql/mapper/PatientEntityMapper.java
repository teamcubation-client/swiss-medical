package com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.entity.PatientEntity;

public class PatientEntityMapper {

  public static Patient toDomain(PatientEntity patientEntity) {
    Patient patient = new Patient();
    patient.setId(patientEntity.getId());
    patient.setFirstName(patientEntity.getFirstName());
    patient.setLastName(patientEntity.getLastName());
    patient.setEmail(patientEntity.getEmail());
    patient.setPhoneNumber(patientEntity.getPhoneNumber());
    patient.setDni(patientEntity.getDni());
    patient.setMemberNumber(patientEntity.getMemberNumber());
    patient.setBirthDate(patientEntity.getBirthDate());
    patient.setActive(patientEntity.isActive());
    patient.setSocialSecurity(patientEntity.getSocialSecurity());

    return patient;
  }

  public static PatientEntity toEntity(Patient patient) {
    PatientEntity patientEntity = new PatientEntity();
    patientEntity.setId(patient.getId());
    patientEntity.setFirstName(patient.getFirstName());
    patientEntity.setLastName(patient.getLastName());
    patientEntity.setEmail(patient.getEmail());
    patientEntity.setPhoneNumber(patient.getPhoneNumber());
    patientEntity.setDni(patient.getDni());
    patientEntity.setMemberNumber(patient.getMemberNumber());
    patientEntity.setBirthDate(patient.getBirthDate());
    patientEntity.setActive(patient.isActive());
    patientEntity.setSocialSecurity(patient.getSocialSecurity());

    return patientEntity;
  }
}
