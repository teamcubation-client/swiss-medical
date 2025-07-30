package com.tq.pacientes.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldExecuteCompletePatientFlow() throws Exception {

        PatientRequest newPatient = PatientRequest.builder()
                .firstName("María")
                .lastName("González")
                .dni("87654321")
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance("SWISS")
                .healthPlan("Premium")
                .address("Av. Libertador 1234")
                .phoneNumber("1187654321")
                .email("maria@mail.com")
                .build();

        MvcResult creationResult = mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("María"))
                .andReturn();

        PatientResponse createdPatient = objectMapper.readValue(
                creationResult.getResponse().getContentAsString(),
                PatientResponse.class
        );
        Long idPatient = createdPatient.getId();

        mockMvc.perform(get("/api/patients/dni/{dni}", "87654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("87654321"));

        PatientRequest patientUpdate = PatientRequest.builder()
                .firstName("María")
                .lastName("González")
                .dni("87654321")
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance("SWISS")
                .healthPlan("Gold")
                .address("Av. Libertador 1234")
                .phoneNumber("1187654321")
                .email("maria@mail.com")
                .build();

        mockMvc.perform(patch("/api/patients/{id}", idPatient)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.healthPlan").value("Gold"));

        mockMvc.perform(get("/api/patients/health-insurance")
                        .param("healthInsurance", "SWISS")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].healthInsurance").value("SWISS"));

        mockMvc.perform(delete("/api/patients/{id}", idPatient))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/patients/{id}", idPatient))
                .andExpect(status().isNoContent());

        mockMvc.perform(patch("/api/patients/{id}/activate", idPatient))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/patients/{id}", idPatient))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idPatient));
    }

    @Test
    void shouldPerformAdvancedPatientSearches() throws Exception {

        createTestPatient("Juan", "Pérez", "11111111");
        createTestPatient("Juan", "García", "22222222");
        createTestPatient("Ana", "López", "33333333");

        mockMvc.perform(get("/api/patients/first-name/{firstName}", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        mockMvc.perform(get("/api/patients")
                        .param("firstName", "Juan")
                        .param("dni", "11111111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[0].dni").value("11111111"));

        mockMvc.perform(get("/api/patients/first-name/{firstName}", "NoExiste"))
                .andExpect(status().isNoContent());
    }

    private void createTestPatient(String nombre, String apellido, String dni) throws Exception {
        PatientRequest paciente = PatientRequest.builder()
                .firstName(nombre)
                .lastName(apellido)
                .dni(dni)
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance("SWISS")
                .healthPlan("Basico")
                .address("Calle 123")
                .phoneNumber("1122334455")
                .email(nombre.toLowerCase() + "@mail.com")
                .build();

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isCreated());
    }
}
