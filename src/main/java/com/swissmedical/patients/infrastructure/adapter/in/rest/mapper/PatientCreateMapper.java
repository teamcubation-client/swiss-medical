package com.swissmedical.patients.infrastructure.adapter.in.rest.mapper;

import com.swissmedical.patients.application.domain.command.CreatePatientCommand;
import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientCreateDto;

import java.time.LocalDate;

public class PatientCreateMapper {

  public static CreatePatientCommand toCommand(PatientCreateDto patientCreateDto) {
    return new CreatePatientCommand(
            patientCreateDto.getFirstName(),
            patientCreateDto.getLastName(),
            patientCreateDto.getEmail(),
            patientCreateDto.getPhoneNumber(),
            patientCreateDto.getDni(),
            patientCreateDto.getMemberNumber(),
            LocalDate.parse(patientCreateDto.getBirthDate()),
            patientCreateDto.isActive(),
            patientCreateDto.getSocialSecurity()
    );
  }
  public static Patient toDomain(PatientCreateDto patientCreateDto) {
    return Patient.builder()
            .firstName(patientCreateDto.getFirstName())
            .lastName(patientCreateDto.getLastName())
            .email(patientCreateDto.getEmail())
            .phoneNumber(patientCreateDto.getPhoneNumber())
            .dni(patientCreateDto.getDni())
            .memberNumber(patientCreateDto.getMemberNumber())
            .birthDate(LocalDate.parse(patientCreateDto.getBirthDate()))
            .isActive(patientCreateDto.isActive())
            .socialSecurity(patientCreateDto.getSocialSecurity())
            .build();
  }

  public static PatientCreateDto toDto(Patient patient) {
    return PatientCreateDto.builder()
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
