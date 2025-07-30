package com.tq.pacientes.unit.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.in.PatientUseCase;
import com.tq.pacientes.infrastructure.adapter.in.rest.PatientController;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.tq.pacientes.infrastructure.adapter.in.rest.mapper.PatientRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @MockitoBean
    private PatientUseCase patientUseCase;

    @MockitoBean
    private PatientRestMapper patientRestMapper;

    private Patient patient;
    private PatientRequest patientRequest;
    private PatientResponse patientResponse;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Carlos")
                .dni("12345678")
                .healthInsurance("OSDE")
                .healthPlan("210")
                .address("Calle 123")
                .phoneNumber("1123456789")
                .email("jc@mail.com")
                .active(true)
                .creationDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        patientRequest = PatientRequest.builder()
                .firstName("Juan")
                .lastName("Carlos")
                .dni("12345678")
                .healthInsurance("OSDE")
                .healthPlan("210")
                .address("Calle 123")
                .phoneNumber("1123456789")
                .email("jc@mail.com")
                .build();

        patientResponse = PatientResponse.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Carlos")
                .dni("12345678")
                .healthInsurance("OSDE")
                .healthPlan("210")
                .address("Calle 123")
                .phoneNumber("1123456789")
                .email("jc@mail.com")
                .build();
    }

    @Test
    void create_ShouldReturnCreatedPatient() throws Exception {
        when(patientRestMapper.toDomain(patientRequest)).thenReturn(patient);
        when(patientUseCase.create(patient)).thenReturn(patient);
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void search_ShouldReturnAllPatients() throws Exception {
        when(patientUseCase.getAll()).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void findByDni_ShouldReturnPatient() throws Exception {
        when(patientUseCase.getByDni("12345678")).thenReturn(Optional.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/dni/{dni}", "12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("12345678"));
    }

    @Test
    void findById_ShouldReturnPatient() throws Exception {
        when(patientUseCase.getById(1L)).thenReturn(Optional.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getByHealthInsurance_ShouldReturnPaginatedPatients() throws Exception {
        when(patientUseCase.searchByHealthInsurancePaginated("OSDE", 10, 0)).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/health-insurance")
                        .param("healthInsurance", "OSDE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].healthInsurance").value("OSDE"));
    }

    @Test
    void update_ShouldReturnUpdatedPatient() throws Exception {
        when(patientRestMapper.toDomain(patientRequest)).thenReturn(patient);
        when(patientUseCase.update(eq(1L), any())).thenReturn(patient);
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(patch("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(patientUseCase).delete(1L);

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void activate_ShouldReturnOk() throws Exception {
        doNothing().when(patientUseCase).activate(1L);

        mockMvc.perform(patch("/api/patients/1/activate"))
                .andExpect(status().isOk());
    }

}