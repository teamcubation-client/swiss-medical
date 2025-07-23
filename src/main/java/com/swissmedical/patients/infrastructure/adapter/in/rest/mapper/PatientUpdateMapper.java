package com.swissmedical.patients.infrastructure.adapter.in.rest.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;

import java.time.LocalDate;

public class PatientUpdateMapper {

  public static Patient toDomain(PatientUpdateDto patientUpdateDto) {
    return Patient.builder()
            .firstName(patientUpdateDto.getFirstName())
            .lastName(patientUpdateDto.getLastName())
            .email(patientUpdateDto.getEmail())
            .phoneNumber(patientUpdateDto.getPhoneNumber())
            .dni(patientUpdateDto.getDni())
            .socialSecurity(patientUpdateDto.getSocialSecurity())
            .memberNumber(patientUpdateDto.getMemberNumber())
            .birthDate(LocalDate.parse(patientUpdateDto.getBirthDate()))
            .isActive(patientUpdateDto.getIsActive())
            .build();
  }

  public static PatientUpdateDto toDto(Patient patient) {
    return PatientUpdateDto.builder()
            .firstName(patient.getFirstName())
            .lastName(patient.getLastName())
            .email(patient.getEmail())
            .phoneNumber(patient.getPhoneNumber())
            .dni(patient.getDni())
            .socialSecurity(patient.getSocialSecurity())
            .memberNumber(patient.getMemberNumber())
            .birthDate(patient.getBirthDate().toString())
            .isActive(patient.isActive())
            .build();
  }
}
