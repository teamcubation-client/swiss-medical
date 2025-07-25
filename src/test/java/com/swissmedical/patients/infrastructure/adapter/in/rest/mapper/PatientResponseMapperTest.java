package com.swissmedical.patients.infrastructure.adapter.in.rest.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

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

    // Assuming you have a mapper class to convert DTO to model
    Patient patientModel = PatientResponseMapper.toDomain(patient);

    // Assertions to verify the mapping
    assert patientModel != null;
    assert "John".equals(patientModel.getFirstName());
    assert "Doe".equals(patientModel.getLastName());
    assert "john@gmail.com".equals(patientModel.getEmail());
    assert "1234567890".equals(patientModel.getPhoneNumber());
    assert "12345678".equals(patientModel.getDni());
    assert "MEM12345".equals(patientModel.getMemberNumber());
    assert "1990-01-01".equals(patientModel.getBirthDate().toString());
    assert patientModel.isActive();
    assert "Swiss Medical".equals(patientModel.getSocialSecurity());
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
    PatientResponseDto patientResponseDto = PatientResponseMapper.toDto(patient);

    // Assertions to verify the mapping
    assert patientResponseDto != null;
    assert 1L == patientResponseDto.getId();
    assert "Jane".equals(patientResponseDto.getFirstName());
    assert "Doe".equals(patientResponseDto.getLastName());
    assert "jane@gmail.com".equals(patientResponseDto.getEmail());
    assert "0987654321".equals(patientResponseDto.getPhoneNumber());
    assert "87654321".equals(patientResponseDto.getDni());
    assert "MEM54321".equals(patientResponseDto.getMemberNumber());
    assert "1992-02-02".equals(patientResponseDto.getBirthDate());
    assert !patientResponseDto.isActive();
    assert "Swiss Medical".equals(patientResponseDto.getSocialSecurity());

  }
}
