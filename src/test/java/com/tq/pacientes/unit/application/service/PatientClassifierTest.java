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

class PatientClassifierTest {

    private PatientClassifier classifier;
    private Patient patient;

    private static final int SENIOR_AGE = 65;
    private static final int ADULT_AGE = 30;
    private static final int LEGAL_AGE = 18;
    private static final int YOUNG_AGE = 10;

    @BeforeEach
    void setUp() {
        classifier = new PatientClassifier();
        patient = new Patient();
    }


    @Test
    void shouldClassifyAsSenior_whenPatientIsSenior() {
        patient.setBirthDate(LocalDate.now().minusYears(SENIOR_AGE));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.SENIOR, result);
    }

    @Test
    void shouldClassifyAsAdult_whenPatientIsAdult() {
        patient.setBirthDate(LocalDate.now().minusYears(ADULT_AGE));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.ADULT, result);
    }

    @Test
    void shouldClassifyAsYoung_whenPatientIsYoung() {
        patient.setBirthDate(LocalDate.now().minusYears(YOUNG_AGE));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.YOUNG, result);
    }

    @ParameterizedTest
    @ValueSource(ints = {65, 70, 80, 100})
    void shouldClassifyAsSenior_whenPatientAgeIsGreaterThan(int age){
        patient.setBirthDate(LocalDate.now().minusYears(age));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.SENIOR, result);
    }

    @Test
    void shouldThrowException_whenBirthDateIsNull() {
        patient.setBirthDate(null);
        assertThrows(MissingBirthDateException.class, () -> classifier.classify(patient));
    }

    @Test
    void shouldThrowException_whenBirthDateIsFuture() {
        patient.setBirthDate(LocalDate.now().plusDays(1));

        assertThrows(InvalidPatientAgeException.class, () -> classifier.classify(patient));
    }

    @Test
    void shouldClassifyAsYoung_whenPatientIsZeroYearsOld() {
        patient.setBirthDate(LocalDate.now());

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.YOUNG, result);
    }

    @Test
    void shouldClassifyAsAdultAtLegalAge() {
        patient.setBirthDate(LocalDate.now().minusYears(LEGAL_AGE));

        PatientType result = classifier.classify(patient);

        assertEquals(PatientType.ADULT, result);
    }


}

