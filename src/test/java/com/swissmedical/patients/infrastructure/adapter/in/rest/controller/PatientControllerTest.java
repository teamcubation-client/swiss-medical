package com.swissmedical.patients.infrastructure.adapter.in.rest.controller;

import com.swissmedical.patients.application.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PatientService patientService;

  @Test
  public void testGetAllPatients() throws Exception {
    when(patientService.getAll(anyString(), anyInt(), anyInt())).thenReturn(List.of());

    mockMvc.perform(get("/api/patients")
                    .param("name", "")
                    .param("page", "1")
                    .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  public void testGetPatientByDni() throws Exception {
    when(patientService.getByDni(anyString())).thenReturn(null);

    mockMvc.perform(get("/api/patients/dni/{dni}", "12345678"))
            .andExpect(status().isNotFound());
  }

  @Test
  public void testCreatePatient() throws Exception {
    // Add your test logic here
  }

  @Test
  public void testUpdatePatient() throws Exception {
    // Add your test logic here
  }

  @Test
  public void testDeletePatient() throws Exception {
    // Add your test logic here
  }
}
