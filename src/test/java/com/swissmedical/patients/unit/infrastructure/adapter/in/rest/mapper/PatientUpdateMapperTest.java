package com.swissmedical.patients.unit.infrastructure.adapter.in.rest.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientUpdateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class PatientUpdateMapperTest {

  @Test
  public void testMapToModel() {
    PatientUpdateDto patientUpdateDto = PatientUpdateDto.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john@gmail.com")
            .phoneNumber("1234567890")
            .dni("12345678")
            .memberNumber("MEM12345")
            .birthDate("1990-01-01")
            .isActive(true)
            .socialSecurity("Swiss Medical")
            .build();

    // Assuming you have a mapper class to convert DTO to model
    Patient patient = PatientUpdateMapper.toDomain(patientUpdateDto);

    // Assertions to verify the mapping
    assertEquals("John", patient.getFirstName());
    assertEquals("Doe", patient.getLastName());
    assertEquals("john@gmail.com", patient.getEmail());
    assertEquals("1234567890", patient.getPhoneNumber());
    assertEquals("12345678", patient.getDni());
    assertEquals("MEM12345", patient.getMemberNumber());
    assertEquals("1990-01-01", patient.getBirthDate().toString());
    assertEquals(true, patient.isActive());
    assertEquals("Swiss Medical", patient.getSocialSecurity());
  }

  @Test
  public void testMapToDto() {
    Patient patient = Patient.builder()
            .id(1L)
            .firstName("Jane")
            .lastName("Doe")
            .email("jane@gmail.com")
            .phoneNumber("0987654321")
            .dni("87654321")
            .memberNumber("MEM54321")
            .birthDate(LocalDate.of(1992, 2, 2))
            .isActive(false)
            .socialSecurity("Swiss Medical")
            .build();

    // Assuming you have a mapper class to convert model to DTO
    PatientUpdateDto patientUpdateDto = PatientUpdateMapper.toDto(patient);

    // Assertions to verify the mapping
    assertEquals("Jane", patientUpdateDto.getFirstName());
    assertEquals("Doe", patientUpdateDto.getLastName());
    assertEquals("jane@gmail.com", patientUpdateDto.getEmail());
    assertEquals("0987654321", patientUpdateDto.getPhoneNumber());
    assertEquals("87654321", patientUpdateDto.getDni());
    assertEquals("MEM54321", patientUpdateDto.getMemberNumber());
    assertEquals("1992-02-02", patientUpdateDto.getBirthDate());
    assertFalse(patientUpdateDto.getIsActive());
    assertEquals("Swiss Medical", patientUpdateDto.getSocialSecurity());
  }
}
