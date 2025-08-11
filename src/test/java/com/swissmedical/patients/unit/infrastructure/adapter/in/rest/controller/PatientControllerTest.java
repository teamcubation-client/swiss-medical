package com.swissmedical.patients.unit.infrastructure.adapter.in.rest.controller;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.service.PatientService;
import com.swissmedical.patients.infrastructure.adapter.in.rest.controller.PatientController;
import com.swissmedical.patients.shared.exceptions.PatientDuplicateException;
import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;
import com.swissmedical.patients.unit.shared.utils.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PatientService patientService;

  private Patient patientJohn;
  private Patient patientJane;

  @BeforeEach
  void setUp() {
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
  void testGetAllPatients() throws Exception {
    when(patientService.getAll(anyString(), anyInt(), anyInt())).thenReturn(List.of());

    mockMvc.perform(get("/api/patients")
                    .param("name", "")
                    .param("page", "1")
                    .param("size", "10"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetAllPatientsWithPagination() throws Exception {
    when(patientService.getAll(anyString(), anyInt(), anyInt())).thenReturn(List.of(
            patientJohn,
            patientJane
    ));

    mockMvc.perform(get("/api/patients")
                    .param("name", "")
                    .param("page", "1")
                    .param("size", "10"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetAllPatientsWithName() throws Exception {
    String name = "John";

    when(patientService.getAll(anyString(), anyInt(), anyInt())).thenReturn(List.of(
            patientJohn,
            patientJane
    ));

    mockMvc.perform(get("/api/patients")
                    .param("name", name)
                    .param("page", "1")
                    .param("size", "10"))
            .andExpect(status().isOk());
  }

  @Test
  void testGetAllPatientsWithPaginationBadRequest() throws Exception {

    when(patientService.getAll(anyString(), anyInt(), anyInt())).thenThrow(new IllegalArgumentException("Invalid page or size"));

    mockMvc.perform(get("/api/patients")
                    .param("name", "")
                    .param("page", "-1")
                    .param("size", "10"))
            .andExpect(status().isBadRequest());
  }

  @Test
  void testGetAllPatientsWithNameNotFound() throws Exception {
    String name = "NonExistent";

    when(patientService.getAll(anyString(), anyInt(), anyInt())).thenThrow(
            new PatientNotFoundException("Patient not found")
    );

    mockMvc.perform(get("/api/patients")
                    .param("name", name)
                    .param("page", "1")
                    .param("size", "10"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetPatientByDni() throws Exception {
    when(patientService.getByDni(anyString())).thenReturn(patientJohn);

    mockMvc.perform(get("/api/patients/dni/{dni}", TestConstants.DNI))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.email").value(TestConstants.EMAIL))
            .andExpect(jsonPath("$.dni").value(TestConstants.DNI));
  }

  @Test
  void testGetPatientByDniNotFound() throws Exception {
    when(patientService.getByDni(anyString())).thenThrow(new PatientNotFoundException("Patient not found"));

    mockMvc.perform(get("/api/patients/dni/{dni}", TestConstants.DNI))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetPatientsBySocialSecurity() throws Exception {
    when(patientService.getBySocialSecurity(anyString(), anyInt(), anyInt())).thenReturn(List.of(patientJohn));

    mockMvc.perform(get("/api/patients/social-security/{socialSecurity}", TestConstants.SOCIAL_SECURITY)
                    .param("page", "1")
                    .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].firstName").value("John"))
            .andExpect(jsonPath("$[0].lastName").value("Doe"))
            .andExpect(jsonPath("$[0].email").value(TestConstants.EMAIL))
            .andExpect(jsonPath("$[0].dni").value(TestConstants.DNI));
  }

  @Test
  void testGetPatientsBySocialSecurityNotFound() throws Exception {
    when(patientService.getBySocialSecurity(anyString(), anyInt(), anyInt())).thenThrow(
            new PatientNotFoundException("Patient not found")
    );

    mockMvc.perform(get("/api/patients/social-security/{socialSecurity}", TestConstants.SOCIAL_SECURITY)
                    .param("page", "1")
                    .param("size", "10"))
            .andExpect(status().isNoContent());
  }

  @Test
  void testGetPatientsBySocialSecurityWithPagination() throws Exception {
    when(patientService.getBySocialSecurity(anyString(), anyInt(), anyInt())).thenReturn(List.of(
            patientJohn,
            patientJane
    ));

    mockMvc.perform(get("/api/patients/social-security/{socialSecurity}", TestConstants.SOCIAL_SECURITY)
                    .param("page", "1")
                    .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].firstName").value("John"))
            .andExpect(jsonPath("$[0].lastName").value("Doe"))
            .andExpect(jsonPath("$[0].email").value(TestConstants.EMAIL))
            .andExpect(jsonPath("$[0].dni").value(TestConstants.DNI));
  }

  @Test
  void testGetPatientsBySocialSecurityWithPaginationBadRequest() throws Exception {

    when(patientService.getBySocialSecurity(anyString(), anyInt(), anyInt())).thenThrow(
            new IllegalArgumentException("Invalid page or size")
    );

    mockMvc.perform(get("/api/patients/social-security/{socialSecurity}", TestConstants.SOCIAL_SECURITY)
                    .param("page", "-1")
                    .param("size", "10"))
            .andExpect(status().isBadRequest());
  }

  @Test
  void testCreatePatientBadRequest() throws Exception {
    mockMvc.perform(post("/api/patients")
                    .contentType("application/json")
                    .content("{\"firstName\":\"\",\"lastName\":\"Doe\",\"email\":\"" + TestConstants.EMAIL + "\"," +
                            "\"phoneNumber\":\"1234567890\",\"dni\":\"" + TestConstants.DNI +
                            "\",\"memberNumber\":\"MEM12345\",\"birthDate\":\"1990-01-01\",\"isActive\":true}"))
            .andExpect(status().isBadRequest());
  }

  @Test
  void testDeletePatient() throws Exception {
    mockMvc.perform(delete("/api/patients/{id}", TestConstants.ID))
            .andExpect(status().isNoContent());
  }
}
