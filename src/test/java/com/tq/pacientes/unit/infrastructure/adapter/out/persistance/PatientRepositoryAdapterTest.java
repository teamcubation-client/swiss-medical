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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PatientRepositoryAdapterTest {

    @Mock
    private PatientRepositoryJpa repositoryJpa;

    @InjectMocks
    private PatientPersistenceAdapter adapter;

    private Patient patient;
    private PatientEntity patientEntity;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .dni("12345678")
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance("OSDE")
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
                .firstName("Juan")
                .lastName("Pérez")
                .dni("12345678")
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance("OSDE")
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
    void shouldSaveNewPatient() {
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
    void shouldUpdateExistingPatient() {
        when(repositoryJpa.save(any(PatientEntity.class))).thenReturn(patientEntity);

        Patient updated = adapter.update(patient);

        assertAll(
                () -> assertNotNull(updated),
                () -> assertEquals(patient.getId(), updated.getId()),
                () -> verify(repositoryJpa, times(1)).save(any(PatientEntity.class))
        );
    }

    @Test
    void shouldFindByDni() {
        when(repositoryJpa.findByDni("12345678")).thenReturn(Optional.of(patientEntity));

        Optional<Patient> found = adapter.findByDni("12345678");

        assertAll(
                () -> assertTrue(found.isPresent()),
                () -> assertEquals("12345678", found.get().getDni()),
                () -> verify(repositoryJpa, times(1)).findByDni("12345678")
        );
    }

    @Test
    void shouldReturnEmptyOptionalWhenDniNotFound() {
        when(repositoryJpa.findByDni("99999999")).thenReturn(Optional.empty());

        Optional<Patient> found = adapter.findByDni("99999999");

        assertAll(
                () -> assertFalse(found.isPresent()),
                () -> verify(repositoryJpa, times(1)).findByDni("99999999")
        );
    }

    @Test
    void shouldFindAllPatients() {
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
    void shouldSearchByFirstName() {
        List<PatientEntity> entities = List.of(patientEntity);
        when(repositoryJpa.findByFirstNameContainingIgnoreCase("Juan"))
                .thenReturn(entities);

        Deque<Patient> patients = new LinkedList<>(adapter.searchByFirstName("Juan"));

        assertAll(
                () -> assertFalse(patients.isEmpty()),
                () -> assertEquals("Juan", patients.getFirst().getFirstName()),
                () -> verify(repositoryJpa, times(1)).findByFirstNameContainingIgnoreCase("Juan")
        );
    }

    @Test
    void shouldSearchByHealthInsurancePaginated() {
        List<PatientEntity> entities = List.of(patientEntity);
        when(repositoryJpa.findByHealthInsurancePaginated("OSDE", 10, 0))
                .thenReturn(entities);

        Deque<Patient> patients = new LinkedList<>(adapter.searchByHealthInsurancePaginated("OSDE", 10, 0));

        assertAll(
                () -> assertFalse(patients.isEmpty()),
                () -> assertEquals("OSDE", patients.getFirst().getHealthInsurance()),
                () -> verify(repositoryJpa, times(1)).findByHealthInsurancePaginated("OSDE", 10, 0)
        );
    }

    @Test
    void shouldFindById() {
        when(repositoryJpa.findById(1L)).thenReturn(Optional.of(patientEntity));

        Optional<Patient> found = adapter.findById(1L);

        assertAll(
                () -> assertTrue(found.isPresent()),
                () -> assertEquals(1L, found.get().getId()),
                () -> verify(repositoryJpa, times(1)).findById(1L)
        );
    }

    @Test
    void shouldPreserveAllFieldsWhenMapping() {
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