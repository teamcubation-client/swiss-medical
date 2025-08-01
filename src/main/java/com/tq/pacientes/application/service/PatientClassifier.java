package com.tq.pacientes.application.service;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.model.PatientType;
import com.tq.pacientes.shared.exceptions.InvalidPatientAgeException;
import org.springframework.stereotype.Component;

import static com.tq.pacientes.shared.util.PatientConstants.LEGAL_AGE;
import static com.tq.pacientes.shared.util.PatientConstants.BASE_SENIOR_AGE;
import static com.tq.pacientes.shared.util.PatientConstants.BASE_YOUNG_AGE;
import static com.tq.pacientes.shared.util.PatientUtils.calculateAge;

@Component
public class PatientClassifier {

    public PatientType classify(Patient patient) {
        int age = calculateAge(patient);
        if (age >= BASE_SENIOR_AGE) return PatientType.SENIOR;
        if (age >= LEGAL_AGE) return PatientType.ADULT;
        if (age >= BASE_YOUNG_AGE) return PatientType.YOUNG;
        throw new InvalidPatientAgeException(age);
    }
}

