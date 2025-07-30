package com.tq.pacientes.unit.application.domain.model.patientprocessing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.model.PatientType;
import com.tq.pacientes.application.domain.model.patientprocessing.*;
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
    void shouldCreateSeniorStrategy() {
        when(classifier.classify(patient)).thenReturn(PatientType.SENIOR);

        PatientSaveTemplate strategy = factory.getPatientSaveStrategy(patient, repositoryPort);

        assertAll(
                () -> assertNotNull(strategy),
                () -> assertInstanceOf(SeniorPatientSave.class, strategy),
                () -> verify(classifier, times(1)).classify(patient)
        );
    }

    @Test
    void shouldCreateAdultStrategy() {
        when(classifier.classify(patient)).thenReturn(PatientType.ADULT);

        PatientSaveTemplate strategy = factory.getPatientSaveStrategy(patient, repositoryPort);

        assertAll(
                () -> assertNotNull(strategy),
                () -> assertInstanceOf(AdultPatientSave.class, strategy),
                () -> verify(classifier, times(1)).classify(patient)
        );
    }

    @Test
    void shouldCreateYoungStrategy() {
        when(classifier.classify(patient)).thenReturn(PatientType.YOUNG);

        PatientSaveTemplate strategy = factory.getPatientSaveStrategy(patient, repositoryPort);

        assertAll(
                () -> assertNotNull(strategy),
                () -> assertInstanceOf(YoungPatientSave.class, strategy),
                () -> verify(classifier, times(1)).classify(patient)
        );
    }

    @Test
    void shouldHandleNullPatient() {
        assertThrows(IllegalArgumentException.class, () ->
                factory.getPatientSaveStrategy(null, repositoryPort)
        );

        verify(classifier, never()).classify(any(Patient.class));
    }

    @Test
    void shouldHandleNullRepository() {
        assertThrows(IllegalArgumentException.class, () ->
                factory.getPatientSaveStrategy(patient, null)
        );

        verify(classifier, never()).classify(any(Patient.class));
    }

    @Test
    void shouldUseClassifierToGetType() {
        when(classifier.classify(patient)).thenReturn(PatientType.ADULT);

        factory.getPatientSaveStrategy(patient, repositoryPort);

        verify(classifier, times(1)).classify(patient);
    }

    @Test
    void shouldHandleClassifierExceptions() {
        when(classifier.classify(patient)).thenThrow(new InvalidPatientAgeException(0));

        assertThrows(InvalidPatientAgeException.class, () ->
                factory.getPatientSaveStrategy(patient, repositoryPort)
        );

        verify(classifier, times(1)).classify(patient);
    }
}