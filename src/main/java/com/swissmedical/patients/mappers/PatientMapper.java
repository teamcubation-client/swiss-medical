package com.swissmedical.patients.mappers;

import com.swissmedical.patients.dto.PatientDto;
import com.swissmedical.patients.entity.Patient;

public class PatientMapper {

    static public Patient toEntity(PatientDto patientDto) {
        if (patientDto == null) {
            return null;
        }

        Patient patient = new Patient();
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setEmail(patientDto.getEmail());
        patient.setPhoneNumber(patientDto.getPhoneNumber());
        patient.setDni(patientDto.getDni());
        patient.setSocialSecurity(patientDto.getSocialSecurity());

        return patient;
    }
}
