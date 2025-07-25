package com.tq.pacientes.infrastructure.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.service.PatientService;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.tq.pacientes.infrastructure.adapter.in.rest.mapper.PatientRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private PatientRestMapper patientRestMapper;

    private Patient patient;
    private PatientRequest patientRequest;
    private PatientResponse patientResponse;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .dni("12345678")
                .healthInsurance("OSDE")
                .healthPlan("210")
                .address("123 St")
                .phoneNumber("1123456789")
                .email("john@example.com")
                .active(true)
                .creationDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        patientRequest = PatientRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .dni("12345678")
                .healthInsurance("OSDE")
                .healthPlan("210")
                .address("123 St")
                .phoneNumber("1123456789")
                .email("john@example.com")
                .build();

        patientResponse = PatientResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .dni("12345678")
                .healthInsurance("OSDE")
                .healthPlan("210")
                .address("123 St")
                .phoneNumber("1123456789")
                .email("john@example.com")
                .active(true)
                .build();
    }

    @Test
    void create_ShouldReturnCreatedPatient() throws Exception {
        when(patientRestMapper.toDomain(patientRequest)).thenReturn(patient);
        when(patientService.create(patient)).thenReturn(patient);
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void search_ShouldReturnAllPatients() throws Exception {
        when(patientService.getAll()).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void findByDni_ShouldReturnPatient() throws Exception {
        when(patientService.getByDni("12345678")).thenReturn(Optional.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/dni/{dni}", "12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("12345678"));
    }

    @Test
    void findById_ShouldReturnPatient() throws Exception {
        when(patientService.getById(1L)).thenReturn(Optional.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getByHealthInsurance_ShouldReturnPaginatedPatients() throws Exception {
        when(patientService.searchByHealthInsurancePaginated("OSDE", 10, 0)).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/health-insurance")
                        .param("healthInsurance", "OSDE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].healthInsurance").value("OSDE"));
    }

    @Test
    void update_ShouldReturnUpdatedPatient() throws Exception {
        when(patientRestMapper.toDomain(patientRequest)).thenReturn(patient);
        when(patientService.update(eq(1L), any())).thenReturn(patient);
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(patch("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(patientService).delete(1L);

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void activate_ShouldReturnOk() throws Exception {
        doNothing().when(patientService).activate(1L);

        mockMvc.perform(patch("/api/patients/1/activate"))
                .andExpect(status().isOk());
    }

}