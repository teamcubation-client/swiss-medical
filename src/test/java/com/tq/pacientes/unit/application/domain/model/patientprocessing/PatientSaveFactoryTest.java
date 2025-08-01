package com.tq.pacientes.unit.application.domain.model.patientprocessing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.model.PatientType;
import com.tq.pacientes.application.domain.model.patient.processing.*;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import com.tq.pacientes.application.service.PatientClassifier;
import com.tq.pacientes.shared.exceptions.InvalidPatientAgeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PatientSaveFactoryTest {

    @Mock
    private PatientClassifier classifier;

    @Mock
    private PatientRepositoryPort repositoryPort;

    private PatientSaveFactory factory;
    private Patient patient;

    @BeforeEach
    void setUp() {
        factory = new PatientSaveFactory(classifier);
        patient = Patient.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Carlos")
                .dni("12345678")
                .build();
    }

    @Test
    void shouldCreateSeniorStrategy_whenPatientIsSenior() {
        when(classifier.classify(patient)).thenReturn(PatientType.SENIOR);

        PatientSaveTemplate strategy = factory.createStrategyFor(patient, repositoryPort);

        assertAll(
                () -> assertNotNull(strategy),
                () -> assertInstanceOf(SeniorPatientSave.class, strategy),
                () -> verify(classifier, times(1)).classify(patient)
        );
    }

    @Test
    void shouldCreateAdultStrategy_whenPatientIsAdult() {
        when(classifier.classify(patient)).thenReturn(PatientType.ADULT);

        PatientSaveTemplate strategy = factory.createStrategyFor(patient, repositoryPort);

        assertAll(
                () -> assertNotNull(strategy),
                () -> assertInstanceOf(AdultPatientSave.class, strategy),
                () -> verify(classifier, times(1)).classify(patient)
        );
    }

    @Test
    void shouldCreateYoungStrategy_whenPatientIsYoung() {
        when(classifier.classify(patient)).thenReturn(PatientType.YOUNG);

        PatientSaveTemplate strategy = factory.createStrategyFor(patient, repositoryPort);

        assertAll(
                () -> assertNotNull(strategy),
                () -> assertInstanceOf(YoungPatientSave.class, strategy),
                () -> verify(classifier, times(1)).classify(patient)
        );
    }

    @Test
    void shouldHandleNullPatient_whenPatientIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                factory.createStrategyFor(null, repositoryPort)
        );

        verify(classifier, never()).classify(any(Patient.class));
    }

    @Test
    void shouldHandleNullRepository_whenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                factory.createStrategyFor(patient, null)
        );

        verify(classifier, never()).classify(any(Patient.class));
    }

    @Test
    void shouldCallClassifier_whenPatientIsNotNull() {
        when(classifier.classify(patient)).thenReturn(PatientType.ADULT);

        factory.createStrategyFor(patient, repositoryPort);

        verify(classifier, times(1)).classify(patient);
    }

    @Test
    void shouldThrowException_whenPatientAgeIsInvalid() {
        when(classifier.classify(patient)).thenThrow(new InvalidPatientAgeException(0));

        assertThrows(InvalidPatientAgeException.class, () ->
                factory.createStrategyFor(patient, repositoryPort)
        );

        verify(classifier, times(1)).classify(patient);
    }
}