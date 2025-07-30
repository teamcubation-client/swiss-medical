package com.swissmedical.patients.unit.infrastructure.adapter.in.rest.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientResponseDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PatientResponseMapperTest {

  @Test
  public void testMapToModel() {
    PatientResponseDto patient = PatientResponseDto.builder()
            .id(1L)
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

    Patient patientModel = PatientResponseMapper.toDomain(patient);

    assertEquals("John", patientModel.getFirstName());
    assertEquals("Doe", patientModel.getLastName());
    assertEquals("john@gmail.com", patientModel.getEmail());
    assertEquals("1234567890", patientModel.getPhoneNumber());
    assertEquals("12345678", patientModel.getDni());
    assertEquals("MEM12345", patientModel.getMemberNumber());
    assertEquals("1990-01-01", patientModel.getBirthDate().toString());
    assertEquals(true, patientModel.isActive());
    assertEquals("Swiss Medical", patientModel.getSocialSecurity());
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

    PatientResponseDto patientResponseDto = PatientResponseMapper.toDto(patient);

    assertEquals(1L, patientResponseDto.getId());
    assertEquals("Jane", patientResponseDto.getFirstName());
    assertEquals("Doe", patientResponseDto.getLastName());
    assertEquals("jane@gmail.com", patientResponseDto.getEmail());
    assertEquals("0987654321", patientResponseDto.getPhoneNumber());
    assertEquals("87654321", patientResponseDto.getDni());
    assertEquals("MEM54321", patientResponseDto.getMemberNumber());
    assertEquals("1992-02-02", patientResponseDto.getBirthDate());
    assertEquals(false, patientResponseDto.isActive());
    assertEquals("Swiss Medical", patientResponseDto.getSocialSecurity());

  }
}
