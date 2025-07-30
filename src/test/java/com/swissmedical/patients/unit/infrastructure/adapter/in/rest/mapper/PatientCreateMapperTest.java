package com.swissmedical.patients.unit.infrastructure.adapter.in.rest.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientCreateDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientCreateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PatientCreateMapperTest {

  @Test
  public void testMapToModel() {
    PatientCreateDto patientCreateDto = PatientCreateDto.builder()
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

    Patient patient = PatientCreateMapper.toDomain(patientCreateDto);

    assertEquals("John", patient.getFirstName());
    assertEquals("Doe", patient.getLastName());
    assertEquals("john@gmail.com", patient.getEmail());
    assertEquals("1234567890", patient.getPhoneNumber());
    assertEquals("12345678", patient.getDni());
    assertEquals("MEM12345", patient.getMemberNumber());
    assertEquals(LocalDate.of(1990, 1, 1), patient.getBirthDate());
    assertEquals(true, patient.isActive());
    assertEquals("Swiss Medical", patient.getSocialSecurity());
  }

  @Test
  public void testMapToDto() {
    Patient patient = Patient.builder()
            .id(1L)
            .firstName("Jane")
            .lastName("Doe")
            .email("john@gmail.com")
            .phoneNumber("1234567890")
            .dni("12345678")
            .memberNumber("MEM12345")
            .birthDate(LocalDate.of(1990, 1, 1))
            .isActive(true)
            .socialSecurity("Swiss Medical")
            .build();

    PatientCreateDto patientCreateDto = PatientCreateMapper.toDto(patient);

    assertEquals("Jane", patientCreateDto.getFirstName());
    assertEquals("Doe", patientCreateDto.getLastName());
    assertEquals("john@gmail.com", patientCreateDto.getEmail());
    assertEquals("1234567890", patientCreateDto.getPhoneNumber());
    assertEquals("12345678", patientCreateDto.getDni());
    assertEquals("MEM12345", patientCreateDto.getMemberNumber());
    assertEquals("1990-01-01", patientCreateDto.getBirthDate());
    assertEquals(true, patientCreateDto.isActive());
    assertEquals("Swiss Medical", patientCreateDto.getSocialSecurity());
  }
}
