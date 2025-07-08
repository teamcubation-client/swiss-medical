package com.swissmedical.patients.mappers;

import java.time.LocalDate;

import com.swissmedical.patients.dto.PatientCreateDto;
import com.swissmedical.patients.dto.PatientUpdateDto;
import com.swissmedical.patients.entity.Patient;

public class PatientMapper {

    static public Patient toEntity(PatientCreateDto patientCreateDto) {
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

    static public Patient toEntity(PatientUpdateDto patientUpdateDto) {
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
}
