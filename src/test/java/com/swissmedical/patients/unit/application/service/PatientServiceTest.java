package com.swissmedical.patients.unit.application.service;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.domain.ports.out.PatientRepositoryPort;
import com.swissmedical.patients.application.service.PatientService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

  private Patient patient;

  @Mock
  private PatientRepositoryPort repository;

  @InjectMocks
  private PatientService patientService;

  @BeforeEach
  void setUp() {
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
  void testGetAll() {
    when(repository.findAll(10, 0)).thenReturn(List.of(
            Patient.builder().firstName("John").lastName("Doe").build(),
            Patient.builder().firstName("Jane").lastName("Doe").build()
    ));

    List<Patient> patients = patientService.getAll("", 1, 10);

    assertEquals(2, patients.size());
    verify(repository, times(1)).findAll(10, 0);
  }

  @Test
  void testGetAllWithInvalidPage() {
    assertThrows(IllegalArgumentException.class, () -> {
      patientService.getAll("", -1, 10);
    });
    verify(repository, never()).findAll(anyInt(), anyInt());
  }

  @Test
  void testGetAllWithName() {
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
  void testGetAllWithNameNotFound() {
    String name = "NonExistent";
    when(repository.findByFirstName(name)).thenReturn(List.of());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.getAll(name, 1, 10);
    });
    verify(repository, times(1)).findByFirstName(name);
  }

  @Test
  void testGetByDni() {
    when(repository.findByDni(TestConstants.DNI)).thenReturn(Optional.of(patient));
    Patient result = patientService.getByDni(TestConstants.DNI);

    assertEquals(patient, result);
    verify(repository, times(1)).findByDni(TestConstants.DNI);
  }

  @Test
  void testGetByDniNotFound() {
    when(repository.findByDni(TestConstants.DNI)).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.getByDni(TestConstants.DNI);
    });
    verify(repository, times(1)).findByDni(TestConstants.DNI);
  }

  @Test
  void testGetBySocialSecurity() {
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
  void testGetBySocialSecurityNotFound() {
    String socialSecurity = "swiss-medical";

    when(repository.findBySocialSecurity(socialSecurity, 10, 0)).thenReturn(List.of());

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.getBySocialSecurity(socialSecurity, 1, 10);
    });
    verify(repository, times(1)).findBySocialSecurity(socialSecurity, 10, 0);
  }

  @Test
  void testDeletePatient() {
    when(repository.existsById(TestConstants.ID)).thenReturn(true);

    patientService.delete(TestConstants.ID);

    verify(repository, times(1)).delete(TestConstants.ID);
  }

  @Test
  void testDeletePatientNotFound() {
    when(repository.existsById(TestConstants.ID)).thenReturn(false);

    assertThrows(PatientNotFoundException.class, () -> {
      patientService.delete(TestConstants.ID);
    });
    verify(repository, times(1)).existsById(TestConstants.ID);
  }
}
