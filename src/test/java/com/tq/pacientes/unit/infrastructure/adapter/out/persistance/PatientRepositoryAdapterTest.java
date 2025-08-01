package com.tq.pacientes.unit.infrastructure.adapter.out.persistance;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.infrastructure.adapter.out.persistence.PatientEntity;
import com.tq.pacientes.infrastructure.adapter.out.persistence.PatientPersistenceAdapter;
import com.tq.pacientes.infrastructure.adapter.out.persistence.PatientRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PatientRepositoryAdapterTest {

    @Mock
    private PatientRepositoryJpa repositoryJpa;

    @InjectMocks
    private PatientPersistenceAdapter adapter;

    private Patient patient;
    private PatientEntity patientEntity;
    private final LocalDateTime now = LocalDateTime.now();

    private static final String PATIENT_ID = "1";
    private static final String FIRST_NAME = "Juan";
    private static final String LAST_NAME = "Pérez";
    private static final String EXISTING_DNI = "12345678";
    private static final String HEALTH_INSURANCE_OSDE = "OSDE";

    private static final String NOT_EXISTING_DNI = "99999999";

    private static final int PAGE_SIZE = 10;
    private static final int PAGE_OFFSET = 0;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(Long.valueOf(PATIENT_ID))
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .dni(EXISTING_DNI)
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance(HEALTH_INSURANCE_OSDE)
                .healthPlan("210")
                .address("Calle Falsa 123")
                .phoneNumber("1234567890")
                .email("juan@mail.com")
                .active(true)
                .creationDate(now)
                .lastModifiedDate(now)
                .build();

        patientEntity = PatientEntity.builder()
                .id(1L)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .dni(EXISTING_DNI)
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance(HEALTH_INSURANCE_OSDE)
                .healthPlan("210")
                .address("Calle Falsa 123")
                .phoneNumber("1234567890")
                .email("juan@mail.com")
                .active(true)
                .creationDate(now)
                .lastModifiedDate(now)
                .build();
    }

    @Test
    void shouldSaveNewPatient_whenPatientIsNotNull() {
        when(repositoryJpa.save(any(PatientEntity.class))).thenReturn(patientEntity);

        Patient saved = adapter.save(patient);

        assertAll(
                () -> assertNotNull(saved),
                () -> assertEquals(patient.getId(), saved.getId()),
                () -> assertEquals(patient.getFirstName(), saved.getFirstName()),
                () -> verify(repositoryJpa, times(1)).save(any(PatientEntity.class))
        );
    }

    @Test
    void shouldUpdateExistingPatient_whenPatientIsNotNull() {
        when(repositoryJpa.save(any(PatientEntity.class))).thenReturn(patientEntity);

        Patient updated = adapter.update(patient);

        assertAll(
                () -> assertNotNull(updated),
                () -> assertEquals(patient.getId(), updated.getId()),
                () -> verify(repositoryJpa, times(1)).save(any(PatientEntity.class))
        );
    }

    @Test
    void shouldFindByDni_whenDniExists() {
        when(repositoryJpa.findByDni(EXISTING_DNI)).thenReturn(Optional.of(patientEntity));

        Optional<Patient> found = adapter.findByDni(EXISTING_DNI);

        assertAll(
                () -> assertTrue(found.isPresent()),
                () -> assertEquals(EXISTING_DNI, found.get().getDni()),
                () -> verify(repositoryJpa, times(1)).findByDni(EXISTING_DNI)
        );
    }

    @Test
    void shouldReturnEmptyOptional_whenDniNotFound() {
        when(repositoryJpa.findByDni(NOT_EXISTING_DNI)).thenReturn(Optional.empty());

        Optional<Patient> found = adapter.findByDni(NOT_EXISTING_DNI);

        assertAll(
                () -> assertFalse(found.isPresent()),
                () -> verify(repositoryJpa, times(1)).findByDni(NOT_EXISTING_DNI)
        );
    }

    @Test
    void shouldFindAllPatients_whenThereArePatients() {
        List<PatientEntity> entities = List.of(patientEntity);
        when(repositoryJpa.findAll()).thenReturn(entities);

        List<Patient> patients = adapter.findAll();

        assertAll(
                () -> assertFalse(patients.isEmpty()),
                () -> assertEquals(1, patients.size()),
                () -> verify(repositoryJpa, times(1)).findAll()
        );
    }

    @Test
    void shouldSearchByFirstName_whenThereArePatientsWithThatName() {
        List<PatientEntity> entities = List.of(patientEntity);
        when(repositoryJpa.findByFirstNameContainingIgnoreCase(FIRST_NAME))
                .thenReturn(entities);

        Deque<Patient> patients = new LinkedList<>(adapter.searchByFirstName(FIRST_NAME));

        assertAll(
                () -> assertFalse(patients.isEmpty()),
                () -> assertEquals(FIRST_NAME, patients.getFirst().getFirstName()),
                () -> verify(repositoryJpa, times(1)).findByFirstNameContainingIgnoreCase(FIRST_NAME)
        );
    }

    @Test
    void shouldSearchByHealthInsurancePaginated_whenThereArePatientsWithThatHealthInsurance() {
        List<PatientEntity> entities = List.of(patientEntity);
        when(repositoryJpa.findByHealthInsurancePaginated(
                HEALTH_INSURANCE_OSDE,
                PAGE_SIZE,
                PAGE_OFFSET
        ))
                .thenReturn(entities);

        Deque<Patient> patients = new LinkedList<>(adapter.searchByHealthInsurancePaginated(
                HEALTH_INSURANCE_OSDE,
                PAGE_SIZE,
                PAGE_OFFSET
        ));

        assertAll(
                () -> assertFalse(patients.isEmpty()),
                () -> assertEquals(HEALTH_INSURANCE_OSDE, patients.getFirst().getHealthInsurance()),
                () -> verify(repositoryJpa, times(1)).findByHealthInsurancePaginated(
                        HEALTH_INSURANCE_OSDE,
                        PAGE_SIZE,
                        PAGE_OFFSET
                ));
    }

    @Test
    void shouldFindById_whenIdExists() {
        when(repositoryJpa.findById(Long.valueOf(PATIENT_ID))).thenReturn(Optional.of(patientEntity));

        Optional<Patient> found = adapter.findById(Long.valueOf(PATIENT_ID));

        assertAll(
                () -> assertTrue(found.isPresent()),
                () -> assertEquals(Long.valueOf(PATIENT_ID), found.get().getId()),
                () -> verify(repositoryJpa, times(1)).findById(Long.valueOf(PATIENT_ID))
        );
    }

    @Test
    void shouldPreserveAllFieldsWhenMapping_whenPatientIsNotNull() {
        when(repositoryJpa.save(any(PatientEntity.class))).thenReturn(patientEntity);

        Patient saved = adapter.save(patient);

        assertAll(
                () -> assertEquals(patient.getId(), saved.getId()),
                () -> assertEquals(patient.getFirstName(), saved.getFirstName()),
                () -> assertEquals(patient.getLastName(), saved.getLastName()),
                () -> assertEquals(patient.getDni(), saved.getDni()),
                () -> assertEquals(patient.getBirthDate(), saved.getBirthDate()),
                () -> assertEquals(patient.getHealthInsurance(), saved.getHealthInsurance()),
                () -> assertEquals(patient.getHealthPlan(), saved.getHealthPlan()),
                () -> assertEquals(patient.getAddress(), saved.getAddress()),
                () -> assertEquals(patient.getPhoneNumber(), saved.getPhoneNumber()),
                () -> assertEquals(patient.getEmail(), saved.getEmail()),
                () -> assertEquals(patient.getActive(), saved.getActive()),
                () -> assertEquals(patient.getCreationDate(), saved.getCreationDate()),
                () -> assertEquals(patient.getLastModifiedDate(), saved.getLastModifiedDate()),
                () -> verify(repositoryJpa, times(1)).save(any(PatientEntity.class))
        );
    }
}