package com.tq.pacientes.shared.util;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.shared.exceptions.InvalidPatientAgeException;
import com.tq.pacientes.shared.exceptions.MissingBirthDateException;

import java.time.LocalDate;
import java.time.Period;

public class PatientUtils {
    public static int calculateAge(Patient patient) {
        if (patient.getBirthDate() == null) {
            throw new MissingBirthDateException();
        }

        if (patient.getBirthDate().isAfter(LocalDate.now())) {
            throw new InvalidPatientAgeException(Integer.MIN_VALUE);
        }
        return Period.between(patient.getBirthDate(), LocalDate.now()).getYears();
    }
}
