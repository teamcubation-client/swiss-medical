package com.swissmedical.patients.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.domain.ports.out.PatientRepositoryPort;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientCreateDto;
import com.swissmedical.patients.unit.shared.utils.TestContants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientsApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @MockitoSpyBean
  private PatientRepositoryPort patientRepositoryPort;

  @Autowired
  private ObjectMapper objectMapper;

  private Patient patient;

  @BeforeEach
  public void setUp() {

  }

  @Test
  @Rollback
  public void createPatient() throws Exception {
    PatientCreateDto patientCreateDto = PatientCreateDto.builder()
            .firstName("John")
            .lastName("Doe")
            .email(TestContants.EMAIL)
            .phoneNumber("1234567890")
            .dni("12345678")
            .memberNumber("MEM12345")
            .birthDate("1990-01-01")
            .isActive(true)
            .socialSecurity("Swiss Medical")
            .build();

    doReturn(false).when(patientRepositoryPort).existsByDni("12345678");
    doReturn(false).when(patientRepositoryPort).existsByEmail(TestContants.EMAIL);

    mockMvc.perform(post("/api/patients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(patientCreateDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("John"));

    verify(patientRepositoryPort).existsByDni("12345678");
    verify(patientRepositoryPort).existsByEmail(TestContants.EMAIL);
    verify(patientRepositoryPort).save(any(Patient.class));
  }
}
