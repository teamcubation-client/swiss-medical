package com.swissmedical.patients.infrastructure.adapter.in.rest.mapper;

import java.time.LocalDate;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientCreateDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientResponseDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;


public class PatientResponseMapper {

    static public PatientResponseDto toResponseDto(Patient patient) {
        PatientResponseDto responseDto = new PatientResponseDto();
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
