package com.swissmedical.patients.application.domain.factory;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.shared.exceptions.PatientInvalidException;

import java.time.LocalDate;

public interface PatientFactory {
    Patient create(
            Long id,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String dni,
            String memberNumber,
            LocalDate birthDate,
            boolean isActive,
            String socialSecurity
    ) throws PatientInvalidException;
}