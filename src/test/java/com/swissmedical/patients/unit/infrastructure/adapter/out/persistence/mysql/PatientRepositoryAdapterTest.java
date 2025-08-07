package com.swissmedical.patients.unit.infrastructure.adapter.out.persistence.mysql;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.PatientJpaRepository;
import com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.PatientRepositoryAdapter;
import com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.mapper.PatientEntityMapper;
import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;
import com.swissmedical.patients.unit.shared.utils.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientRepositoryAdapterTest {

  @Mock
  private PatientJpaRepository patientJpaRepository;

  @InjectMocks
  private PatientRepositoryAdapter patientRepositoryAdapter;

  @Spy
  private PatientEntityMapper patientEntityMapper;

  private Patient patientJohn;
  private Patient patientJane;

  @BeforeEach
  public void setUp() {
    patientJohn = Patient.builder()
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
            patientEntityMapper.toEntity(patientJohn),
            patientEntityMapper.toEntity(patientJane)
    ));

    Deque<Patient> patients = new LinkedList<>(patientRepositoryAdapter.findAll(10, 0));

    assertEquals(2, patients.size());
    assertEquals(TestConstants.DNI, patients.getFirst().getDni());
    assertEquals(TestConstants.EMAIL, patients.getFirst().getEmail());
    verify(patientJpaRepository, times(1)).findAll(anyInt(), anyInt());
  }

  @Test
  public void testFindAllWithEmptyResult() {
    when(patientJpaRepository.findAll(anyInt(), anyInt())).thenReturn(List.of());

    Deque<Patient> patients = new LinkedList<>(patientRepositoryAdapter.findAll(10, 0));

    assertEquals(0, patients.size());
    verify(patientJpaRepository, times(1)).findAll(anyInt(), anyInt());
  }

  @Test
  public void testFindByFirstName() {
    when(patientJpaRepository.findByFirstName(TestConstants.FIRST_NAME)).thenReturn(List.of(
            patientEntityMapper.toEntity(patientJohn)
    ));

    Deque<Patient> patients = new LinkedList<>(patientRepositoryAdapter.findByFirstName(TestConstants.FIRST_NAME));

    assertEquals(1, patients.size());
    assertEquals(TestConstants.FIRST_NAME, patients.getFirst().getFirstName());
    verify(patientJpaRepository, times(1)).findByFirstName(TestConstants.FIRST_NAME);
  }

  @Test
  public void testFindByFirstNameNotFound() {
    when(patientJpaRepository.findByFirstName(TestConstants.FIRST_NAME)).thenReturn(List.of());

    Deque<Patient> patients = new LinkedList<>(patientRepositoryAdapter.findByFirstName(TestConstants.FIRST_NAME));

    assertEquals(0, patients.size());
    verify(patientJpaRepository, times(1)).findByFirstName(TestConstants.FIRST_NAME);
  }

  @Test
  public void testFindBySocialSecurity() {
    when(patientJpaRepository.findBySocialSecurity(TestConstants.SOCIAL_SECURITY, 10, 0)).thenReturn(List.of(
            patientEntityMapper.toEntity(patientJohn)
    ));

    Deque<Patient> patients =
            new LinkedList<>(patientRepositoryAdapter.findBySocialSecurity(TestConstants.SOCIAL_SECURITY,
                    10,
                    0));

    assertEquals(1, patients.size());
    assertEquals(TestConstants.SOCIAL_SECURITY, patients.getFirst().getSocialSecurity());
    verify(patientJpaRepository, times(1)).findBySocialSecurity(TestConstants.SOCIAL_SECURITY, 10, 0);
  }

  @Test
  public void testFindBySocialSecurityNotFound() {
    when(patientJpaRepository.findBySocialSecurity(TestConstants.SOCIAL_SECURITY, 10, 0)).thenReturn(List.of());

    Deque<Patient> patients =
            new LinkedList<>(patientRepositoryAdapter.findBySocialSecurity(TestConstants.SOCIAL_SECURITY,
                    10,
                    0));

    assertEquals(0, patients.size());
    verify(patientJpaRepository, times(1)).findBySocialSecurity(TestConstants.SOCIAL_SECURITY, 10, 0);
  }

  @Test
  public void testFindByDni() {
    when(patientJpaRepository.findByDni(TestConstants.DNI)).thenReturn(
            Optional.of(patientEntityMapper.toEntity(patientJohn))
    );

    Optional<Patient> patient = patientRepositoryAdapter.findByDni(TestConstants.DNI);

    assertTrue(patient.isPresent());
    assertEquals(TestConstants.DNI, patient.get().getDni());
    verify(patientJpaRepository, times(1)).findByDni(TestConstants.DNI);
  }

  @Test
  public void testFindByDniNotFound() {
    when(patientJpaRepository.findByDni(TestConstants.DNI)).thenReturn(Optional.empty());

    Optional<Patient> patient = patientRepositoryAdapter.findByDni(TestConstants.DNI);

    assertFalse(patient.isPresent());
    verify(patientJpaRepository, times(1)).findByDni(TestConstants.DNI);
  }

  @Test
  public void testFindById() {
    when(patientJpaRepository.findById(TestConstants.ID)).thenReturn(
            Optional.of(patientEntityMapper.toEntity(patientJohn))
    );

    Optional<Patient> patient = patientRepositoryAdapter.findById(TestConstants.ID);

    assertTrue(patient.isPresent());
    assertEquals(TestConstants.DNI, patient.get().getDni());
    verify(patientJpaRepository, times(1)).findById(TestConstants.ID);
  }

  @Test
  public void testFindByIdNotFound() {
    when(patientJpaRepository.findById(TestConstants.ID)).thenReturn(Optional.empty());

    Optional<Patient> patient = patientRepositoryAdapter.findById(TestConstants.ID);

    assertFalse(patient.isPresent());
    verify(patientJpaRepository, times(1)).findById(TestConstants.ID);
  }

  @Test
  public void testExistsByDni() {
    when(patientJpaRepository.existsByDni(TestConstants.DNI)).thenReturn(true);

    boolean exists = patientRepositoryAdapter.existsByDni(TestConstants.DNI);

    assertTrue(exists);
    verify(patientJpaRepository, times(1)).existsByDni(TestConstants.DNI);
  }

  @Test
  public void testExistsByDniNotFound() {
    when(patientJpaRepository.existsByDni(TestConstants.DNI)).thenReturn(false);

    boolean exists = patientRepositoryAdapter.existsByDni(TestConstants.DNI);

    assertFalse(exists);
    verify(patientJpaRepository, times(1)).existsByDni(TestConstants.DNI);
  }

  @Test
  public void testExistsByEmail() {
    when(patientJpaRepository.existsByEmail(TestConstants.EMAIL)).thenReturn(true);

    boolean exists = patientRepositoryAdapter.existsByEmail(TestConstants.EMAIL);

    assertTrue(exists);
    verify(patientJpaRepository, times(1)).existsByEmail(TestConstants.EMAIL);
  }

  @Test
  public void testExistsByEmailNotFound() {
    when(patientJpaRepository.existsByEmail(TestConstants.EMAIL)).thenReturn(false);

    boolean exists = patientRepositoryAdapter.existsByEmail(TestConstants.EMAIL);

    assertFalse(exists);
    verify(patientJpaRepository, times(1)).existsByEmail(TestConstants.EMAIL);
  }

  @Test
  public void testExistsById() {
    when(patientJpaRepository.existsById(TestConstants.ID)).thenReturn(true);

    boolean exists = patientRepositoryAdapter.existsById(TestConstants.ID);

    assertTrue(exists);
    verify(patientJpaRepository, times(1)).existsById(TestConstants.ID);
  }

  @Test
  public void testExistsByIdNotFound() {
    when(patientJpaRepository.existsById(TestConstants.ID)).thenReturn(false);

    boolean exists = patientRepositoryAdapter.existsById(TestConstants.ID);

    assertFalse(exists);
    verify(patientJpaRepository, times(1)).existsById(TestConstants.ID);
  }

  @Test
  public void testSave() {
    when(patientJpaRepository.save(patientEntityMapper.toEntity(patientJohn)))
            .thenReturn(patientEntityMapper.toEntity(patientJohn));

    Patient savedPatient = patientRepositoryAdapter.save(patientJohn);

    assertEquals(TestConstants.DNI, savedPatient.getDni());
    assertEquals(TestConstants.EMAIL, savedPatient.getEmail());
    verify(patientJpaRepository, times(1)).save(patientEntityMapper.toEntity(patientJohn));
  }

  @Test
  public void testSaveWithDuplicateDni() {
    when(patientJpaRepository.save(patientEntityMapper.toEntity(patientJohn)))
            .thenThrow(new RuntimeException("Duplicate DNI"));

    assertThrows(RuntimeException.class, () -> patientRepositoryAdapter.save(patientJohn));
  }

  @Test
  public void testUpdate() {
    when(patientJpaRepository.existsById(anyLong())).thenReturn(true);
    when(patientJpaRepository.save(any()))
            .thenReturn(patientEntityMapper.toEntity(patientJohn));

    Patient updatedPatient = patientRepositoryAdapter.update(TestConstants.ID, patientJohn);

    assertEquals(TestConstants.DNI, updatedPatient.getDni());
    assertEquals(TestConstants.EMAIL, updatedPatient.getEmail());
    verify(patientJpaRepository, times(1)).existsById(anyLong());
    verify(patientJpaRepository, times(1)).save(any());
  }

  @Test
  public void testUpdateNotFound() {
    when(patientJpaRepository.existsById(anyLong())).thenReturn(false);

    assertThrows(PatientNotFoundException.class, () -> patientRepositoryAdapter.update(TestConstants.ID, patientJohn));
  }

  @Test
  public void testDelete() {
    when(patientJpaRepository.existsById(TestConstants.ID)).thenReturn(true);

    patientRepositoryAdapter.delete(TestConstants.ID);

    verify(patientJpaRepository, times(1)).deleteById(TestConstants.ID);
  }

  @Test
  public void testDeleteNotFound() {
    when(patientJpaRepository.existsById(TestConstants.ID)).thenReturn(false);

    assertThrows(PatientNotFoundException.class, () -> patientRepositoryAdapter.delete(TestConstants.ID));
  }
}
