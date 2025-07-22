package com.swissmedical.patients.infrastructure.adapter.in.rest.mapper;

import java.time.LocalDate;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientResponseDto;


public class PatientResponseMapper {

  public static Patient toDomain(PatientResponseDto patientResponseDto) {
    return new Patient.PatientBuilder()
            .id(patientResponseDto.getId())
            .firstName(patientResponseDto.getFirstName())
            .lastName(patientResponseDto.getLastName())
            .email(patientResponseDto.getEmail())
            .phoneNumber(patientResponseDto.getPhoneNumber())
            .dni(patientResponseDto.getDni())
            .memberNumber(patientResponseDto.getMemberNumber())
            .birthDate(LocalDate.parse(patientResponseDto.getBirthDate()))
            .isActive(patientResponseDto.isActive())
            .socialSecurity(patientResponseDto.getSocialSecurity())
            .build();
  }

  public static PatientResponseDto toDto(Patient patient) {
    return PatientResponseDto.builder()
            .id(patient.getId())
            .firstName(patient.getFirstName())
            .lastName(patient.getLastName())
            .email(patient.getEmail())
            .phoneNumber(patient.getPhoneNumber())
            .dni(patient.getDni())
            .memberNumber(patient.getMemberNumber())
            .birthDate(patient.getBirthDate().toString())
            .isActive(patient.isActive())
            .socialSecurity(patient.getSocialSecurity())
            .build();
  }
}
