package com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.mapper.PatientEntityMapper;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientRepositoryAdapterTest {

  @Mock
  private PatientJpaRepository patientJpaRepository;

  @InjectMocks
  private PatientRepositoryAdapter patientRepositoryAdapter;

  private Patient patientJohn;
  private Patient patientJane;

  @BeforeEach
  public void setUp() {
    patientJohn = Patient.builder()
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

    patientJane = Patient.builder()
            .firstName("Jane")
            .lastName("Doe")
            .email("jane@gmail.com")
            .phoneNumber("1234567860")
            .dni("12340321")
            .memberNumber("MEM12350")
            .birthDate(LocalDate.of(1991, 1, 1))
            .isActive(true)
            .socialSecurity("OSDE")
            .build();
  }

  @Test
  public void testFindAll() {
    when(patientJpaRepository.findAll(anyInt(), anyInt())).thenReturn(List.of(
            PatientEntityMapper.toEntity(patientJohn),
            PatientEntityMapper.toEntity(patientJane)
    ));

    List<Patient> patients = patientRepositoryAdapter.findAll(10, 0);

    assertEquals(2, patients.size());
    assertEquals(TestContants.DNI, patients.get(0).getDni());
    assertEquals(TestContants.EMAIL, patients.get(0).getEmail());
  }

  @Test
  public void testFindAllWithEmptyResult() {
    when(patientJpaRepository.findAll(anyInt(), anyInt())).thenReturn(List.of());

    List<Patient> patients = patientRepositoryAdapter.findAll(10, 0);

    assertEquals(0, patients.size());
  }

  @Test
  public void testFindByFirstName() {
    when(patientJpaRepository.findByFirstName(TestContants.FIRST_NAME)).thenReturn(List.of(
            PatientEntityMapper.toEntity(patientJohn)
    ));

    List<Patient> patients = patientRepositoryAdapter.findByFirstName(TestContants.FIRST_NAME);

    assertEquals(1, patients.size());
    assertEquals(TestContants.FIRST_NAME, patients.get(0).getFirstName());
  }

  @Test
  public void testFindByFirstNameNotFound() {
    when(patientJpaRepository.findByFirstName(TestContants.FIRST_NAME)).thenReturn(List.of());

    List<Patient> patients = patientRepositoryAdapter.findByFirstName(TestContants.FIRST_NAME);

    assertEquals(0, patients.size());
  }

  @Test
  public void testFindBySocialSecurity() {
    when(patientJpaRepository.findBySocialSecurity(TestContants.SOCIAL_SECURITY, 10, 0)).thenReturn(List.of(
            PatientEntityMapper.toEntity(patientJohn)
    ));

    List<Patient> patients = patientRepositoryAdapter.findBySocialSecurity(TestContants.SOCIAL_SECURITY, 10, 0);

    assertEquals(1, patients.size());
    assertEquals(TestContants.SOCIAL_SECURITY, patients.get(0).getSocialSecurity());
  }

  @Test
  public void testFindBySocialSecurityNotFound() {
    when(patientJpaRepository.findBySocialSecurity(TestContants.SOCIAL_SECURITY, 10, 0)).thenReturn(List.of());

    List<Patient> patients = patientRepositoryAdapter.findBySocialSecurity(TestContants.SOCIAL_SECURITY, 10, 0);

    assertEquals(0, patients.size());
  }

  @Test
  public void testFindByDni() {
    when(patientJpaRepository.findByDni(TestContants.DNI)).thenReturn(
            Optional.of(PatientEntityMapper.toEntity(patientJohn))
    );

    Optional<Patient> patient = patientRepositoryAdapter.findByDni(TestContants.DNI);

    assertEquals(true, patient.isPresent());
    assertEquals(TestContants.DNI, patient.get().getDni());
  }

  @Test
  public void testFindByDniNotFound() {
    when(patientJpaRepository.findByDni(TestContants.DNI)).thenReturn(Optional.empty());

    Optional<Patient> patient = patientRepositoryAdapter.findByDni(TestContants.DNI);

    assertEquals(false, patient.isPresent());
  }

  @Test
  public void testFindById() {
    when(patientJpaRepository.findById(1L)).thenReturn(
            Optional.of(PatientEntityMapper.toEntity(patientJohn))
    );

    Optional<Patient> patient = patientRepositoryAdapter.findById(1L);

    assertEquals(true, patient.isPresent());
    assertEquals(TestContants.DNI, patient.get().getDni());
  }

  @Test
  public void testFindByIdNotFound() {
    when(patientJpaRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<Patient> patient = patientRepositoryAdapter.findById(1L);

    assertEquals(false, patient.isPresent());
  }

  @Test
  public void testExistsByDni() {
    when(patientJpaRepository.existsByDni(TestContants.DNI)).thenReturn(true);

    boolean exists = patientRepositoryAdapter.existsByDni(TestContants.DNI);

    assertEquals(true, exists);
  }

  @Test
  public void testExistsByDniNotFound() {
    when(patientJpaRepository.existsByDni(TestContants.DNI)).thenReturn(false);

    boolean exists = patientRepositoryAdapter.existsByDni(TestContants.DNI);

    assertEquals(false, exists);
  }

  @Test
  public void testExistsByEmail() {
    when(patientJpaRepository.existsByEmail(TestContants.EMAIL)).thenReturn(true);

    boolean exists = patientRepositoryAdapter.existsByEmail(TestContants.EMAIL);

    assertEquals(true, exists);
  }

  @Test
  public void testExistsByEmailNotFound() {
    when(patientJpaRepository.existsByEmail(TestContants.EMAIL)).thenReturn(false);

    boolean exists = patientRepositoryAdapter.existsByEmail(TestContants.EMAIL);

    assertEquals(false, exists);
  }

  @Test
  public void testExistsById() {
    when(patientJpaRepository.existsById(1L)).thenReturn(true);

    boolean exists = patientRepositoryAdapter.existsById(1L);

    assertEquals(true, exists);
  }

  @Test
  public void testExistsByIdNotFound() {
    when(patientJpaRepository.existsById(1L)).thenReturn(false);

    boolean exists = patientRepositoryAdapter.existsById(1L);

    assertEquals(false, exists);
  }

  @Test
  public void testSave() {
    when(patientJpaRepository.save(PatientEntityMapper.toEntity(patientJohn)))
            .thenReturn(PatientEntityMapper.toEntity(patientJohn));

    Patient savedPatient = patientRepositoryAdapter.save(patientJohn);

    assertEquals(TestContants.DNI, savedPatient.getDni());
    assertEquals(TestContants.EMAIL, savedPatient.getEmail());
  }

  @Test
  public void testSaveWithDuplicateDni() {
    when(patientJpaRepository.save(PatientEntityMapper.toEntity(patientJohn)))
            .thenThrow(new RuntimeException("Duplicate DNI"));

    assertThrows(RuntimeException.class, () -> {
      patientRepositoryAdapter.save(patientJohn);
    });
  }

  @Test
  public void testUpdate() {
    when(patientJpaRepository.existsById(anyLong())).thenReturn(true);
    when(patientJpaRepository.save(any()))
            .thenReturn(PatientEntityMapper.toEntity(patientJohn));

    Patient updatedPatient = patientRepositoryAdapter.update(1L, patientJohn);

    assertEquals(TestContants.DNI, updatedPatient.getDni());
    assertEquals(TestContants.EMAIL, updatedPatient.getEmail());
  }

  @Test
  public void testUpdateNotFound() {
    when(patientJpaRepository.existsById(anyLong())).thenReturn(false);

    assertThrows(PatientNotFoundException.class, () -> {
      patientRepositoryAdapter.update(1L, patientJohn);
    });
  }

  @Test
  public void testDelete() {
    when(patientJpaRepository.existsById(1L)).thenReturn(true);

    patientRepositoryAdapter.delete(1L);

    verify(patientJpaRepository, times(1)).deleteById(1L);
  }

  @Test
  public void testDeleteNotFound() {
    when(patientJpaRepository.existsById(1L)).thenReturn(false);

    assertThrows(PatientNotFoundException.class, () -> {
      patientRepositoryAdapter.delete(1L);
    });
  }
}
