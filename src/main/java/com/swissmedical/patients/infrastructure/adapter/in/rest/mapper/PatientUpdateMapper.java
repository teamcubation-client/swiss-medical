package com.swissmedical.patients.infrastructure.adapter.in.rest.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;

import java.time.LocalDate;

public class PatientUpdateMapper {

  static public Patient toDomain(PatientUpdateDto patientUpdateDto) {
    Patient patient = new Patient();
    patient.setFirstName(patientUpdateDto.getFirstName());
    patient.setLastName(patientUpdateDto.getLastName());
    patient.setEmail(patientUpdateDto.getEmail());
    patient.setPhoneNumber(patientUpdateDto.getPhoneNumber());
    patient.setDni(patientUpdateDto.getDni());
    patient.setSocialSecurity(patientUpdateDto.getSocialSecurity());
    patient.setMemberNumber(patientUpdateDto.getMemberNumber());
    patient.setBirthDate(LocalDate.parse(patientUpdateDto.getBirthDate()));
    patient.setActive(patientUpdateDto.getIsActive());

    return patient;
  }

  static public PatientUpdateDto toDto(Patient patient) {
    PatientUpdateDto dto = new PatientUpdateDto();
    dto.setFirstName(patient.getFirstName());
    dto.setLastName(patient.getLastName());
    dto.setEmail(patient.getEmail());
    dto.setPhoneNumber(patient.getPhoneNumber());
    dto.setDni(patient.getDni());
    dto.setSocialSecurity(patient.getSocialSecurity());
    dto.setMemberNumber(patient.getMemberNumber());
    dto.setBirthDate(patient.getBirthDate().toString());
    dto.setIsActive(patient.isActive());

    return dto;
  }
}
