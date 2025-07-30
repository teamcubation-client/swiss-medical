package com.tq.pacientes.unit.application.service;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.model.PatientType;
import com.tq.pacientes.application.service.PatientClassifier;
import com.tq.pacientes.shared.exceptions.InvalidPatientAgeException;
import com.tq.pacientes.shared.exceptions.MissingBirthDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatientClassifierTest {

    private PatientClassifier classifier;
    private Patient patient;

    @BeforeEach
    void setUp() {
        classifier = new PatientClassifier();
        patient = new Patient();
    }


    @Test
    void shouldClassifyAsSenior() {
        patient.setBirthDate(LocalDate.now().minusYears(65));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.SENIOR, result);
    }

    @Test
    void shouldClassifyAsAdult() {
        patient.setBirthDate(LocalDate.now().minusYears(30));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.ADULT, result);
    }

    @Test
    void shouldClassifyAsYoung() {
        patient.setBirthDate(LocalDate.now().minusYears(10));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.YOUNG, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {65, 70, 80, 100})
    void shouldClassifyAsSeniorForVariousAges(int age) {
        patient.setBirthDate(LocalDate.now().minusYears(age));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.SENIOR, result);
    }

    @Test
    void shouldThrowExceptionWhenBirthDateIsNull() {
        patient.setBirthDate(null);
        assertThrows(MissingBirthDateException.class, () -> {
            classifier.classify(patient);
        });
    }

    @Test
    void shouldThrowExceptionWhenBirthDateIsFuture() {
        patient.setBirthDate(LocalDate.now().plusDays(1));

        assertThrows(InvalidPatientAgeException.class, () -> {
            classifier.classify(patient);
        });
    }

    @Test
    void shouldClassifyAsYoungAtZeroYears() {
        patient.setBirthDate(LocalDate.now());

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.YOUNG, result);
    }

    @Test
    void shouldClassifyAsAdultAt18thBirthday() {
        patient.setBirthDate(LocalDate.now().minusYears(18));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.ADULT, result);
    }

    @Test
    void shouldClassifyAsSeniorAt65thBirthday() {
        patient.setBirthDate(LocalDate.now().minusYears(65));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.SENIOR, result);
    }

}

