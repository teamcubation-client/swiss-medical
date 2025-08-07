package com.tq.pacientes.unit.application.service;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.model.patient.processing.PatientSaveFactory;
import com.tq.pacientes.application.domain.model.patient.processing.PatientSaveTemplate;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import com.tq.pacientes.application.service.PatientService;
import com.tq.pacientes.shared.exceptions.DuplicatePatientException;
import com.tq.pacientes.shared.exceptions.PatientAlreadyActiveException;
import com.tq.pacientes.shared.exceptions.PatientDniNotFoundException;
import com.tq.pacientes.shared.exceptions.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepositoryPort repository;

    @Mock
    private PatientSaveFactory patientSaveFactory;

    @Mock
    private PatientSaveTemplate saveTemplate;

    @InjectMocks
    private PatientService service;

    private Patient patient;

    private static final String FIRST_NAME = "Juan";
    private static final String LAST_NAME = "Pérez";
    private static final String EXISTING_DNI = "12345678";
    private static final String HEALTH_INSURANCE_OSDE = "OSDE";
    private static final String NOT_EXISTING_DNI = "99999999";

    private static final String UPDATED_FIRST_NAME = "Carlos";
    private static final String UPDATED_LAST_NAME = "Gómez";
    private static final String UPDATED_DNI = "01234567";
    private static final String UPDATED_HEALTH_INSURANCE = "SWISS";
    private static final String UPDATED_HEALTH_PLAN = "PREMIUM";
    private static final String UPDATED_ADDRESS = "Calle Falsa 123";
    private static final String UPDATED_EMAIL = "carlos@mail.com";
    private static final String UPDATED_PHONE = "34567890";

    private static final int PAGE_SIZE = 10;
    private static final int PAGE_OFFSET = 0;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setDni(EXISTING_DNI);
        patient.setFirstName(FIRST_NAME);
        patient.setLastName(LAST_NAME);
        patient.setActive(true);
        patient.setHealthInsurance(HEALTH_INSURANCE_OSDE);
    }

    @Test
    void shouldCreatePatient_whenDniNotExists() {
        when(repository.findByDni(patient.getDni())).thenReturn(Optional.empty());
        when(patientSaveFactory.createStrategyFor(eq(patient), any(PatientRepositoryPort.class)))
                .thenReturn(saveTemplate);
        doNothing().when(saveTemplate).save(patient);

        Patient saved = service.create(patient);

        assertEquals(patient.getDni(), saved.getDni());
        assertNotNull(saved.getCreationDate());
        assertNotNull(saved.getLastModifiedDate());
        verify(patientSaveFactory, times(1)).createStrategyFor(eq(patient), any(PatientRepositoryPort.class));
        verify(saveTemplate, times(1)).save(patient);
    }


    @Test
    void shouldThrowException_whenCreatingPatientWithDuplicateDni() {
        when(repository.findByDni(patient.getDni())).thenReturn(Optional.of(patient));

        assertThrows(DuplicatePatientException.class, () -> service.create(patient));

        verify(patientSaveFactory, never()).createStrategyFor(eq(patient), any(PatientRepositoryPort.class));
    }

    @Test
    void shouldReturnAllPatients_whenExists() {
        List<Patient> patients = List.of(patient);
        when(repository.findAll()).thenReturn(patients);

        Deque<Patient> patientsDeque = new LinkedList<>(service.getAll());

        assertEquals(1, patientsDeque.size());
        assertEquals(FIRST_NAME, patientsDeque.getFirst().getFirstName());

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldReturnPatientById_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(patient));

        Optional<Patient> result = service.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(FIRST_NAME, result.get().getFirstName());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowException_whenDniNotFound() {
        when(repository.findByDni(NOT_EXISTING_DNI)).thenReturn(Optional.empty());

        assertThrows(PatientDniNotFoundException.class, () -> service.getByDni(NOT_EXISTING_DNI));

        verify(repository, times(1)).findByDni(NOT_EXISTING_DNI);
    }

    @Test
    void shouldReturnPatient_whenDniExists() {
        when(repository.findByDni(EXISTING_DNI)).thenReturn(Optional.of(patient));

        Patient result = service.getByDni(EXISTING_DNI);

        assertNotNull(result);
        assertEquals(FIRST_NAME, result.getFirstName());

        verify(repository, times(1)).findByDni(EXISTING_DNI);
    }

    @Test
    void shouldThrowException_whenPatientNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        Patient details = Patient.builder().build();

        assertThrows(PatientNotFoundException.class, () -> service.update(2L, details));

        verify(repository, times(1)).findById(2L);
    }

    @Test
    void shouldReturnPatientsByFirstName_whenExists() {
        when(repository.searchByFirstName(FIRST_NAME)).thenReturn(List.of(patient));

        Deque<Patient> patients = new LinkedList<>(service.searchByFirstName(FIRST_NAME));

        assertEquals(1, patients.size());
        assertEquals(FIRST_NAME, patients.getFirst().getFirstName());

        verify(repository, times(1)).searchByFirstName(FIRST_NAME);
    }

    @Test
    void shouldReturnPatientsByHealthInsurancePaginated_whenExists() {
        when(repository.searchByHealthInsurancePaginated(
                HEALTH_INSURANCE_OSDE,
                PAGE_SIZE,
                PAGE_OFFSET
        ))
                .thenReturn(List.of(patient));

        Deque<Patient> deque = new LinkedList<>(service.searchByHealthInsurancePaginated(
                HEALTH_INSURANCE_OSDE, PAGE_SIZE, PAGE_OFFSET));

        assertEquals(1, deque.size());
        assertEquals(FIRST_NAME, deque.getFirst().getFirstName());

        verify(repository, times(1)).searchByHealthInsurancePaginated(
                HEALTH_INSURANCE_OSDE,
                PAGE_SIZE,
                PAGE_OFFSET
        );
    }

    @Test
    void shouldUpdateBasicPatientFields_whenPatientExists() {
        Patient updates = Patient.builder()
                .firstName(UPDATED_FIRST_NAME)
                .dni(EXISTING_DNI)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        when(repository.update(any(Patient.class))).thenReturn(updates);

        Patient updated = service.update(1L, updates);

        assertEquals(UPDATED_FIRST_NAME, updated.getFirstName());
        assertEquals(EXISTING_DNI, updated.getDni());
        verify(repository, times(1)).update(any(Patient.class));
    }

    @Test
    void shouldUpdateAllPatientFieldsIncludingDni_whenPatientExists() {
        Patient updates = Patient.builder()
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .dni(UPDATED_DNI)
                .healthInsurance(UPDATED_HEALTH_INSURANCE)
                .healthPlan(UPDATED_HEALTH_PLAN)
                .address(UPDATED_ADDRESS)
                .email(UPDATED_EMAIL)
                .phoneNumber(UPDATED_PHONE)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        when(repository.findByDni(UPDATED_DNI)).thenReturn(Optional.empty());
        when(repository.update(any(Patient.class))).thenReturn(updates);

        Patient updated = service.update(1L, updates);

        assertEquals(UPDATED_FIRST_NAME, updated.getFirstName());
        assertEquals(UPDATED_LAST_NAME, updated.getLastName());
        assertEquals(UPDATED_DNI, updated.getDni());
        assertEquals(UPDATED_HEALTH_INSURANCE, updated.getHealthInsurance());
        assertEquals(UPDATED_HEALTH_PLAN, updated.getHealthPlan());
        assertEquals(UPDATED_ADDRESS, updated.getAddress());
        assertEquals(UPDATED_EMAIL, updated.getEmail());
        assertEquals(UPDATED_PHONE, updated.getPhoneNumber());

        verify(repository, times(1)).findByDni(UPDATED_DNI);
        verify(repository, times(1)).update(any(Patient.class));
    }

    @Test
    void shouldThrowException_whenUpdatingToDuplicateDni() {
        Patient updates = new Patient();
        updates.setDni(UPDATED_DNI);

        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        when(repository.findByDni(UPDATED_DNI)).thenReturn(Optional.of(new Patient()));

        assertThrows(DuplicatePatientException.class, () -> service.update(1L, updates));

        verify(repository, times(1)).findByDni(UPDATED_DNI);
    }

    @Test
    void shouldNotCallFindByDni_whenDniIsTheSame() {
        Patient updates = Patient.builder()
                .dni(EXISTING_DNI)
                .firstName(UPDATED_FIRST_NAME)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        when(repository.update(any(Patient.class))).thenReturn(updates);

        Patient updated = service.update(1L, updates);

        assertEquals(UPDATED_FIRST_NAME, updated.getFirstName());
        verify(repository, never()).findByDni(EXISTING_DNI);
    }

    @Test
    void shouldSoftDeletePatient_whenItsActive() {
        when(repository.findById(1L)).thenReturn(Optional.of(patient));

        service.delete(1L);
        assertFalse(patient.getActive());

        verify(repository, times(1)).update(patient);
    }

    @Test
    void shouldActivatePatient_whenItsNotActive() {
        patient.setActive(false);
        when(repository.findByIdIgnoringActive(1L)).thenReturn(Optional.of(patient));

        service.activate(1L);

        assertTrue(patient.getActive());

        verify(repository, times(1)).update(patient);
    }

    @Test
    void shouldThrowException_whenActivatingAlreadyActivePatient() {
        patient.setActive(true);
        when(repository.findByIdIgnoringActive(1L)).thenReturn(Optional.of(patient));

        assertThrows(PatientAlreadyActiveException.class, () -> service.activate(1L));

        verify(repository, never()).update(patient);
    }
}