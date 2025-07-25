package com.swissmedical.patients.infrastructure.adapter.in.rest.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

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

    // Assuming you have a mapper class to convert DTO to model
    Patient patient = PatientCreateMapper.toDomain(patientCreateDto);

    // Assertions to verify the mapping
    assert patient != null;
    assert "John".equals(patient.getFirstName());
    assert "Doe".equals(patient.getLastName());
    assert "john@gmail.com".equals(patient.getEmail());
    assert "1234567890".equals(patient.getPhoneNumber());
    assert "12345678".equals(patient.getDni());
    assert "MEM12345".equals(patient.getMemberNumber());
    assert LocalDate.of(1990, 1, 1).equals(patient.getBirthDate());
    assert patient.isActive();
    assert "Swiss Medical".equals(patient.getSocialSecurity());
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

    // Assuming you have a mapper class to convert model to DTO
    PatientCreateDto patientCreateDto = PatientCreateMapper.toDto(patient);

    // Assertions to verify the mapping
    assert patientCreateDto != null;
    assert "Jane".equals(patientCreateDto.getFirstName());
    assert "Doe".equals(patientCreateDto.getLastName());
    assert "john@gmail.com".equals(patientCreateDto.getEmail());
    assert "1234567890".equals(patientCreateDto.getPhoneNumber());
    assert "12345678".equals(patientCreateDto.getDni());
    assert "MEM12345".equals(patientCreateDto.getMemberNumber());
    assert "1990-01-01".equals(patientCreateDto.getBirthDate());
    assert patientCreateDto.isActive();
    assert "Swiss Medical".equals(patientCreateDto.getSocialSecurity());
  }
}
