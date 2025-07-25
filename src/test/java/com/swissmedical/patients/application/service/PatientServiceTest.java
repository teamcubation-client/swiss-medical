package com.swissmedical.patients.application.service;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.domain.ports.out.PatientRepositoryPort;
import com.swissmedical.patients.shared.exceptions.PatientDuplicateException;
import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;
import com.swissmedical.patients.shared.utils.TestContants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
            .email(TestContants.EMAIL)
            .phoneNumber("1234567890")
            .dni(TestContants.DNI)
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
  }

  @Test
  public void testGetAllWithInvalidPageAndSize() {
    assertThrows(IllegalArgumentException.class, () -> {
      patientService.getAll("", -1, 10);
    });

    assertThrows(IllegalArgumentException.class, () -> {
      patientService.getAll("", 1, -10);
    });
  }

  @Test
  public void testGetAllWithName() {
    String name = "John";
    when(repository.findByFirstName(name)).thenReturn(List.of(
            Patient.builder().firstName("John").lastName("Doe").build()
    ));

    List<Patient> patients = patientService.getAll(name, 1, 10);

    assertEquals(1, patients.size());
    assertEquals("John", patients.get(0).getFirstName());
  }

  @Test
  public void testGetAllWithNameNotFound() {
    String name = "NonExistent";
    when(repository.findByFirstName(name)).thenReturn(List.of());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.getAll(name, 1, 10);
    });
  }

  @Test
  public void testGetByDni() {
    when(repository.findByDni(TestContants.DNI)).thenReturn(Optional.of(patient));
    Patient result = patientService.getByDni(TestContants.DNI);

    assertEquals(patient, result);
  }

  @Test
  public void testGetByDniNotFound() {
    when(repository.findByDni(TestContants.DNI)).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.getByDni(TestContants.DNI);
    });
  }

  @Test
  public void testGetBySocialSecurity() {
    String socialSecurity = "swiss-medical";

    when(repository.findBySocialSecurity(socialSecurity, 10, 0)).thenReturn(List.of(
            Patient.builder().firstName("John").lastName("Doe").socialSecurity("Swiss Medical").build()
    ));

    List<Patient> patients = patientService.getBySocialSecurity(socialSecurity, 1, 10);

    assertEquals(1, patients.size());
    assertEquals("John", patients.get(0).getFirstName());
  }

  @Test
  public void testGetBySocialSecurityNotFound() {
    String socialSecurity = "swiss-medical";

    when(repository.findBySocialSecurity(socialSecurity, 10, 0)).thenReturn(List.of());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.getBySocialSecurity(socialSecurity, 1, 10);
    });
  }

  @Test
  public void testCreatePatient() {
    when(repository.existsByDni(TestContants.DNI)).thenReturn(false);
    when(repository.existsByEmail(TestContants.EMAIL)).thenReturn(false);
    when(repository.save(any())).thenReturn(patient);

    Patient result = patientService.create(patient);

    // Assertions can be added here to verify the result
    // For example, you can use assertEquals to check if the returned patient matches the created
    assertEquals(patient, result);
  }

  @Test
  public void shouldThrowErrorWhenDuplicateDni() {
    when(repository.existsByDni(TestContants.DNI)).thenReturn(true);

    assertThrows(PatientDuplicateException.class, () -> {
      patientService.create(patient);
    });
  }

  @Test
  public void shouldThrowErrorWhenDuplicateEmail() {
    String email = "john@gmail.com";

    when(repository.existsByDni(patient.getDni())).thenReturn(false);
    when(repository.existsByEmail(email)).thenReturn(true);

    assertThrows(PatientDuplicateException.class, () -> {
      patientService.create(patient);
    });
  }

  @Test
  public void testUpdatePatient() {
    when(repository.findById(TestContants.ID)).thenReturn(Optional.of(patient));
    when(repository.save(any())).thenReturn(patient);

    Patient updatedPatient = patientService.update(1L, patient);

    assertEquals(patient, updatedPatient);
  }

  @Test
  public void testUpdatePatientNotFound() {
    when(repository.findById(TestContants.ID)).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.update(1L, patient);
    });
  }

  @Test
  public void testDeletePatient() {
    when(repository.existsById(TestContants.ID)).thenReturn(true);

    patientService.delete(TestContants.ID);

    verify(repository, times(1)).delete(TestContants.ID);
  }

  @Test
  public void testDeletePatientNotFound() {
    when(repository.existsById(TestContants.ID)).thenReturn(false);

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.delete(TestContants.ID);
    });
  }
}
