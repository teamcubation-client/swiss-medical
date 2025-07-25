package com.swissmedical.patients.infrastructure.adapter.in.rest.mapper;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

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
    assert patient != null;
    assert "John".equals(patient.getFirstName());
    assert "Doe".equals(patient.getLastName());
    assert "john@gmail.com".equals(patient.getEmail());
    assert "1234567890".equals(patient.getPhoneNumber());
    assert "12345678".equals(patient.getDni());
    assert "MEM12345".equals(patient.getMemberNumber());
    assert "1990-01-01".equals(patient.getBirthDate().toString());
    assert patient.isActive();
    assert "Swiss Medical".equals(patient.getSocialSecurity());
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
    assert patientUpdateDto != null;
    assert "Jane".equals(patientUpdateDto.getFirstName());
    assert "Doe".equals(patientUpdateDto.getLastName());
    assert "jane@gmail.com".equals(patientUpdateDto.getEmail());
    assert "0987654321".equals(patientUpdateDto.getPhoneNumber());
    assert "87654321".equals(patientUpdateDto.getDni());
    assert "MEM54321".equals(patientUpdateDto.getMemberNumber());
    assert "1992-02-02".equals(patientUpdateDto.getBirthDate());
    assert !patientUpdateDto.getIsActive();
    assert "Swiss Medical".equals(patientUpdateDto.getSocialSecurity());
  }
}
