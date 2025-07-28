package com.tq.pacientes.application.service;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.model.PatientType;
import com.tq.pacientes.shared.exceptions.InvalidPatientAgeException;
import com.tq.pacientes.shared.exceptions.MissingBirthDateException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

import static com.tq.pacientes.shared.util.PatientUtils.calculateAge;

@Component
public class PatientClassifier {

    public PatientType classify(Patient patient) {
        int age = calculateAge(patient);
        if (age >= 65) return PatientType.SENIOR;
        if (age >= 18) return PatientType.ADULT;
        if (age >= 0) return PatientType.YOUNG;
        throw new InvalidPatientAgeException(age);
    }
}

