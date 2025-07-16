package com.swissmedical.patients.infrastructure.adapter.in.rest.mapper;

import java.time.LocalDate;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientResponseDto;



public class PatientResponseMapper {

    static public Patient toDomain(PatientResponseDto patientResponseDto) {
        Patient patient = new Patient();
        patient.setId(patientResponseDto.getId());
        patient.setFirstName(patientResponseDto.getFirstName());
        patient.setLastName(patientResponseDto.getLastName());
        patient.setEmail(patientResponseDto.getEmail());
        patient.setPhoneNumber(patientResponseDto.getPhoneNumber());
        patient.setDni(patientResponseDto.getDni());
        patient.setSocialSecurity(patientResponseDto.getSocialSecurity());
        patient.setMemberNumber(patientResponseDto.getMemberNumber());
        patient.setBirthDate(LocalDate.parse(patientResponseDto.getBirthDate()));
        patient.setActive(patientResponseDto.isActive());

        return patient;
    }

    static public PatientResponseDto toDto(Patient patient) {
        PatientResponseDto responseDto = new PatientResponseDto();
        responseDto.setId(patient.getId());
        responseDto.setFirstName(patient.getFirstName());
        responseDto.setLastName(patient.getLastName());
        responseDto.setEmail(patient.getEmail());
        responseDto.setPhoneNumber(patient.getPhoneNumber());
        responseDto.setDni(patient.getDni());
        responseDto.setMemberNumber(patient.getMemberNumber());
        responseDto.setBirthDate(patient.getBirthDate().toString());
        responseDto.setActive(patient.isActive());
        responseDto.setSocialSecurity(patient.getSocialSecurity());

        return responseDto;
    }
}
