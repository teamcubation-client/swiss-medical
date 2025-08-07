package com.swissmedical.patients.application.domain.factory;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.domain.validator.PatientValidator;
import com.swissmedical.patients.shared.exceptions.PatientInvalidException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@Component
public class PatientFactoryImpl implements PatientFactory {
    private final PatientValidator patientValidator;
    @Override
    public Patient create(Long id, String firstName, String lastName, String email, String phoneNumber, String dni, String memberNumber, LocalDate birthDate, boolean isActive, String socialSecurity) throws PatientInvalidException {
        Patient patient = Patient.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .dni(dni)
                .memberNumber(memberNumber)
                .birthDate(birthDate)
                .isActive(isActive)
                .socialSecurity(socialSecurity)
                .build();
        patientValidator.validate(patient);
        return patient;
    }
}
