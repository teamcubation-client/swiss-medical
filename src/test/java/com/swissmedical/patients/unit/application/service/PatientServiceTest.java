package com.swissmedical.patients.unit.application.service;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.domain.ports.out.PatientRepositoryPort;
import com.swissmedical.patients.application.service.PatientService;
import com.swissmedical.patients.shared.exceptions.PatientDuplicateException;
import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;
import com.swissmedical.patients.unit.shared.utils.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

  private Patient patient;

  @Mock
  private PatientRepositoryPort repository;

  @InjectMocks
  private PatientService patientService;

  @BeforeEach
  public void setUp() {
    patient = Patient.builder()
            .firstName("John")
            .lastName("Doe")
            .email(TestConstants.EMAIL)
            .phoneNumber("1234567890")
            .dni(TestConstants.DNI)
            .memberNumber("MEM12345")
            .birthDate(LocalDate.of(1990, 1, 1))
            .isActive(true)
            .socialSecurity("Swiss Medical")
            .build();
  }

  @Test
  public void testGetAll() {
    when(repository.findAll(10, 0)).thenReturn(List.of(
            Patient.builder().firstName("John").lastName("Doe").build(),
            Patient.builder().firstName("Jane").lastName("Doe").build()
    ));

    List<Patient> patients = patientService.getAll("", 1, 10);

    assertEquals(2, patients.size());
    verify(repository, times(1)).findAll(10, 0);
  }

  @Test
  public void testGetAllWithInvalidPage() {
    assertThrows(IllegalArgumentException.class, () -> {
      patientService.getAll("", -1, 10);
    });
    verify(repository, never()).findAll(anyInt(), anyInt());
  }

  public void testGetAllWithInvalidSize() {
    assertThrows(IllegalArgumentException.class, () -> {
      patientService.getAll("", 1, -10);
    });

    verify(repository, never()).findAll(anyInt(), anyInt());
  }

  @Test
  public void testGetAllWithName() {
    String name = "John";
    when(repository.findByFirstName(name)).thenReturn(List.of(
            Patient.builder().firstName("John").lastName("Doe").build()
    ));

    Deque<Patient> patients = new LinkedList<>(patientService.getAll(name, 1, 10));

    assertEquals(1, patients.size());
    assertEquals("John", patients.getFirst().getFirstName());
    verify(repository, times(1)).findByFirstName(name);
  }

  @Test
  public void testGetAllWithNameNotFound() {
    String name = "NonExistent";
    when(repository.findByFirstName(name)).thenReturn(List.of());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.getAll(name, 1, 10);
    });
    verify(repository, times(1)).findByFirstName(name);
  }

  @Test
  public void testGetByDni() {
    when(repository.findByDni(TestConstants.DNI)).thenReturn(Optional.of(patient));
    Patient result = patientService.getByDni(TestConstants.DNI);

    assertEquals(patient, result);
    verify(repository, times(1)).findByDni(TestConstants.DNI);
  }

  @Test
  public void testGetByDniNotFound() {
    when(repository.findByDni(TestConstants.DNI)).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.getByDni(TestConstants.DNI);
    });
    verify(repository, times(1)).findByDni(TestConstants.DNI);
  }

  @Test
  public void testGetBySocialSecurity() {
    String socialSecurity = "swiss-medical";

    when(repository.findBySocialSecurity(socialSecurity, 10, 0)).thenReturn(List.of(
            Patient.builder().firstName("John").lastName("Doe").socialSecurity("Swiss Medical").build()
    ));

    Deque<Patient> patients = new LinkedList<>(patientService.getBySocialSecurity(socialSecurity, 1, 10));

    assertEquals(1, patients.size());
    assertEquals("John", patients.getFirst().getFirstName());
    verify(repository, times(1)).findBySocialSecurity(socialSecurity, 10, 0);
  }

  @Test
  public void testGetBySocialSecurityNotFound() {
    String socialSecurity = "swiss-medical";

    when(repository.findBySocialSecurity(socialSecurity, 10, 0)).thenReturn(List.of());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.getBySocialSecurity(socialSecurity, 1, 10);
    });
    verify(repository, times(1)).findBySocialSecurity(socialSecurity, 10, 0);
  }

  @Test
  public void testCreatePatient() {
    when(repository.existsByDni(TestConstants.DNI)).thenReturn(false);
    when(repository.existsByEmail(TestConstants.EMAIL)).thenReturn(false);
    when(repository.save(any())).thenReturn(patient);

    Patient result = patientService.create(patient);

    assertEquals(patient, result);
    verify(repository, times(1)).existsByDni(TestConstants.DNI);
    verify(repository, times(1)).existsByEmail(TestConstants.EMAIL);
    verify(repository, times(1)).save(patient);
  }

  @Test
  public void shouldThrowErrorWhenDuplicateDni() {
    when(repository.existsByDni(TestConstants.DNI)).thenReturn(true);

    assertThrows(PatientDuplicateException.class, () -> {
      patientService.create(patient);
    });
    verify(repository, times(1)).existsByDni(TestConstants.DNI);
  }

  @Test
  public void shouldThrowErrorWhenDuplicateEmail() {
    String email = "john@gmail.com";

    when(repository.existsByDni(patient.getDni())).thenReturn(false);
    when(repository.existsByEmail(email)).thenReturn(true);

    assertThrows(PatientDuplicateException.class, () -> {
      patientService.create(patient);
    });
    verify(repository, times(1)).existsByEmail(email);
  }

  @Test
  public void testUpdatePatient() {
    when(repository.findById(TestConstants.ID)).thenReturn(Optional.of(patient));
    when(repository.save(any())).thenReturn(patient);

    Patient updatedPatient = patientService.update(TestConstants.ID, patient);

    assertEquals(patient, updatedPatient);
    verify(repository, times(1)).findById(TestConstants.ID);
    verify(repository, times(1)).save(patient);
  }

  @Test
  public void testUpdatePatientNotFound() {
    when(repository.findById(TestConstants.ID)).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.update(TestConstants.ID, patient);
    });
    verify(repository, times(1)).findById(TestConstants.ID);
  }

  @Test
  public void testDeletePatient() {
    when(repository.existsById(TestConstants.ID)).thenReturn(true);

    patientService.delete(TestConstants.ID);

    verify(repository, times(1)).delete(TestConstants.ID);
  }

  @Test
  public void testDeletePatientNotFound() {
    when(repository.existsById(TestConstants.ID)).thenReturn(false);

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.delete(TestConstants.ID);
    });
    verify(repository, times(1)).existsById(TestConstants.ID);
  }
}
