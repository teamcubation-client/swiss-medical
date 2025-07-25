package com.tq.pacientes.application.service;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepositoryPort repository;

    @InjectMocks
    private PatientService service;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setDni("12345678");
        patient.setFirstName("Juan");
        patient.setLastName("Pérez");
        patient.setActive(true);
        patient.setHealthInsurance("OSDE");
    }

    @Test
    void shouldCreatePatient_whenDniNotExists() {
        when(repository.findByDni(patient.getDni())).thenReturn(Optional.empty());
        when(repository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient saved = service.create(patient);

        assertEquals(patient.getDni(), saved.getDni());
        verify(repository).save(any(Patient.class));
    }

    @Test
    void shouldThrowException_whenCreatingPatientWithDuplicateDni() {
        when(repository.findByDni(patient.getDni())).thenReturn(Optional.of(patient));

        assertThrows(DuplicatePatientException.class, () -> service.create(patient));
    }

    @Test
    void shouldReturnAllPatients() {
        List<Patient> patients = List.of(patient);
        when(repository.findAll()).thenReturn(patients);

        List<Patient> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
    }

    @Test
    void shouldReturnPatientById() {
        when(repository.findById(1L)).thenReturn(Optional.of(patient));

        Optional<Patient> result = service.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getFirstName());
    }

    @Test
    void shouldThrowException_whenDniNotFound() {
        when(repository.findByDni("99999999")).thenReturn(Optional.empty());

        assertThrows(PatientDniNotFoundException.class, () -> service.getByDni("99999999"));
    }

    @Test
    void shouldReturnPatientWhenDniExists() {
        when(repository.findByDni("12345678")).thenReturn(Optional.of(patient));

        Optional<Patient> result = service.getByDni("12345678");

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getFirstName());
    }

    @Test
    void shouldThrowException_whenPatientNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        Patient details = Patient.builder().dni("22222222").build();
        assertThrows(PatientNotFoundException.class, () -> service.update(2L, details));
    }

    @Test
    void shouldReturnPatientsByFirstName() {
        when(repository.searchByFirstName("Juan")).thenReturn(List.of(patient));

        List<Patient> result = service.searchByFirstName("Juan");

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
    }

    @Test
    void shouldReturnPatientsByHealthInsurancePaginated() {
        when(repository.searchByHealthInsurancePaginated(eq("OSDE"), eq(10), eq(0)))
                .thenReturn(List.of(patient));

        List<Patient> result = service.searchByHealthInsurancePaginated("OSDE", 10, 0);

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
    }

    @Test
    void shouldUpdatePatient() {
        Patient updates = new Patient();
        updates.setFirstName("Carlos");
        updates.setDni("12345678");

        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        when(repository.update(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient updated = service.update(1L, updates);

        assertEquals("Carlos", updated.getFirstName());
    }

    @Test
    void shouldUpdateAllPatientFields() {
        Patient updates = new Patient();
        updates.setFirstName("Carlos");
        updates.setLastName("Gómez");
        updates.setDni("99999999"); // distinto al original
        updates.setHealthInsurance("OSDE");
        updates.setHealthPlan("310");
        updates.setAddress("Calle Falsa 123");
        updates.setEmail("carlos@example.com");
        updates.setPhoneNumber("123456789");

        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        when(repository.findByDni("99999999")).thenReturn(Optional.empty()); // no es duplicado
        when(repository.update(any())).thenAnswer(inv -> inv.getArgument(0));

        Patient updated = service.update(1L, updates);

        assertEquals("Carlos", updated.getFirstName());
        assertEquals("Gómez", updated.getLastName());
        assertEquals("99999999", updated.getDni());
        assertEquals("OSDE", updated.getHealthInsurance());
        assertEquals("310", updated.getHealthPlan());
        assertEquals("Calle Falsa 123", updated.getAddress());
        assertEquals("carlos@example.com", updated.getEmail());
        assertEquals("123456789", updated.getPhoneNumber());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingToDuplicateDni() {
        Patient updates = new Patient();
        updates.setDni("99999999"); // distinto

        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        when(repository.findByDni("99999999")).thenReturn(Optional.of(new Patient())); // ya existe

        assertThrows(DuplicatePatientException.class, () -> service.update(1L, updates));
    }

    @Test
    void shouldNotCallFindByDniWhenDniIsTheSame() {
        Patient updates = new Patient();
        updates.setDni("12345678"); // igual al actual
        updates.setFirstName("Mario");

        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        when(repository.update(any())).thenAnswer(inv -> inv.getArgument(0));

        Patient updated = service.update(1L, updates);

        assertEquals("Mario", updated.getFirstName());
        verify(repository, never()).findByDni("12345678");
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        Patient updates = new Patient();
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> service.update(999L, updates));
    }

    @Test
    void shouldSoftDeletePatient() {
        when(repository.findById(1L)).thenReturn(Optional.of(patient));

        service.delete(1L);

        assertFalse(patient.getActive());
        verify(repository).update(patient);
    }

    @Test
    void shouldActivatePatient() {
        patient.setActive(false);
        when(repository.findById(1L)).thenReturn(Optional.of(patient));

        service.activate(1L);

        assertTrue(patient.getActive());
        verify(repository).update(patient);
    }

    @Test
    void shouldThrowException_whenActivatingAlreadyActivePatient() {
        patient.setActive(true);
        when(repository.findById(1L)).thenReturn(Optional.of(patient));

        assertThrows(PatientAlreadyActiveException.class, () -> service.activate(1L));
    }
}

/*public class PatientServiceTest {

    @Mock
    private PatientRepositoryPort patientRepositoryPort;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePatient_Success() {
        Patient patient = Patient.builder().dni("12345678").firstName("Juan").build();
        when(patientRepositoryPort.findByDni("12345678")).thenReturn(Optional.empty());
        when(patientRepositoryPort.save(any(Patient.class))).thenReturn(patient);

        Patient saved = patientService.create(patient);
        assertEquals("Juan", saved.getFirstName());
        verify(patientRepositoryPort).save(any(Patient.class));
    }

    @Test
    void testCreatePatient_Duplicate() {
        Patient patient = Patient.builder().dni("12345678").build();
        when(patientRepositoryPort.findByDni("12345678")).thenReturn(Optional.of(patient));
        assertThrows(DuplicatePatientException.class, () -> patientService.create(patient));
    }

    @Test
    void testGetByDni_NotFound() {
        when(patientRepositoryPort.findByDni("99999999")).thenReturn(Optional.empty());
        assertThrows(PatientDniNotFoundException.class, () -> patientService.getByDni("99999999"));
    }

    @Test
    void testGetById_Found() {
        Patient patient = Patient.builder().id(1L).dni("12345678").build();
        when(patientRepositoryPort.findById(1L)).thenReturn(Optional.of(patient));
        Optional<Patient> result = patientService.getById(1L);
        assertTrue(result.isPresent());
        assertEquals("12345678", result.get().getDni());
    }

    @Test
    void testUpdatePatient_NotFound() {
        when(patientRepositoryPort.findById(2L)).thenReturn(Optional.empty());
        Patient details = Patient.builder().dni("22222222").build();
        assertThrows(PatientNotFoundException.class, () -> patientService.update(2L, details));
    }

    @Test
    void testDeletePatient_Success() {
        Patient patient = Patient.builder().id(3L).active(true).build();
        when(patientRepositoryPort.findById(3L)).thenReturn(Optional.of(patient));
        when(patientRepositoryPort.update(any(Patient.class))).thenReturn(patient);
        patientService.delete(3L);
        verify(patientRepositoryPort).update(any(Patient.class));
    }

    @Test
    void testGetAllPatients() {
        Patient p1 = Patient.builder().dni("1").build();
        Patient p2 = Patient.builder().dni("2").build();
        when(patientRepositoryPort.findAll()).thenReturn(List.of(p1, p2));
        List<Patient> patients = patientService.getAll();
        assertEquals(2, patients.size());
    }
}*/
