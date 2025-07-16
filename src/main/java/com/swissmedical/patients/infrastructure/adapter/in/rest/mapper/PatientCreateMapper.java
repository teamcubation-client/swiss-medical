package com.swissmedical.patients.infrastructure.adapter.in.rest.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientCreateDto;

import java.time.LocalDate;

public class PatientCreateMapper {

  static public Patient toDomain(PatientCreateDto patientCreateDto) {
    Patient patient = new Patient();
    patient.setFirstName(patientCreateDto.getFirstName());
    patient.setLastName(patientCreateDto.getLastName());
    patient.setEmail(patientCreateDto.getEmail());
    patient.setPhoneNumber(patientCreateDto.getPhoneNumber());
    patient.setDni(patientCreateDto.getDni());
    patient.setSocialSecurity(patientCreateDto.getSocialSecurity());
    patient.setMemberNumber(patientCreateDto.getMemberNumber());
    patient.setBirthDate(LocalDate.parse(patientCreateDto.getBirthDate()));
    patient.setActive(patientCreateDto.isActive());

    return patient;
  }

  public static PatientCreateDto toDto(Patient patient) {
    PatientCreateDto dto = new PatientCreateDto();
    dto.setFirstName(patient.getFirstName());
    dto.setLastName(patient.getLastName());
    dto.setEmail(patient.getEmail());
    dto.setPhoneNumber(patient.getPhoneNumber());
    dto.setDni(patient.getDni());
    dto.setSocialSecurity(patient.getSocialSecurity());
    dto.setMemberNumber(patient.getMemberNumber());
    dto.setBirthDate(patient.getBirthDate().toString());
    dto.setActive(patient.isActive());

    return dto;
  }

}
