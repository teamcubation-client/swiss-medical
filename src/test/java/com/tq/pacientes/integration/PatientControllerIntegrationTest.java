package com.tq.pacientes.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PatientRequest patient1;

    @BeforeEach
    void setUp() {
        patient1 = PatientRequest.builder()
                .firstName("María")
                .lastName("González")
                .dni("87654321")
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance("SWISS")
                .healthPlan("Basic")
                .address("Calle 123")
                .phoneNumber("1234567890")
                .email("maria@mail")
                .build();
    }

    @Test
    void shouldReturnCreatedPatient_whenValidPatientIsPosted() throws Exception {
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("María"));
    }

    @Test
    void shouldReturnPatientByDni_whenPatientExists() throws Exception {
        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient1)));

        mockMvc.perform(get("/api/patients/dni/{dni}", "87654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("87654321"));
    }

    @Test
    void shouldUpdatePatient_whenValidPatchRequest() throws Exception {
        Long idPatient = createPatientAndGetId(patient1);
        PatientRequest patientUpdate = PatientRequest.builder()
                .healthPlan("Gold")
                .build();

        mockMvc.perform(patch("/api/patients/{id}", idPatient)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.healthPlan").value("Gold"));
    }

    @Test
    void shouldReturnPatientsByHealthInsurance_whenValidParams() throws Exception {
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient1)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/patients/health-insurance")
                        .param("healthInsurance", "SWISS")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].healthInsurance").value("SWISS"));
    }

    @Test
    void shouldDeletePatient_whenValidId() throws Exception {
        Long idPatient = createPatientAndGetId(patient1);
        mockMvc.perform(delete("/api/patients/{id}", idPatient))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNoContent_whenPatientNotFoundById() throws Exception {
        mockMvc.perform(get("/api/patients/{id}", 9999L))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldActivatePatient_whenValidId() throws Exception {
        Long idPatient = createPatientAndGetId(patient1);
        mockMvc.perform(delete("/api/patients/{id}", idPatient));
        mockMvc.perform(patch("/api/patients/{id}/activate", idPatient))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnActivatedPatient_whenPatientIsActivated() throws Exception {
        Long idPatient = createPatientAndGetId(patient1);
        mockMvc.perform(delete("/api/patients/{id}", idPatient));
        mockMvc.perform(patch("/api/patients/{id}/activate", idPatient))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/patients/{id}", idPatient))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idPatient));
    }

    @Test
    void shouldReturnPatientsByFirstName_whenPatientsExist() throws Exception {
        PatientRequest patient2 = PatientRequest.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .dni("11111111")
                .birthDate(LocalDate.of(1990, 1, 2))
                .healthInsurance("SWISS")
                .healthPlan("Basic")
                .address("Calle 1")
                .phoneNumber("1111111111")
                .email("juan1@mail.com")
                .build();

        PatientRequest patient3 = PatientRequest.builder()
                .firstName("Juan")
                .lastName("Gómez")
                .dni("22222222")
                .birthDate(LocalDate.of(1991, 1, 3))
                .healthInsurance("SWISS")
                .healthPlan("Premium")
                .address("Calle 2")
                .phoneNumber("2222222222")
                .email("juan2@mail.com")
                .build();

        createPatientAndGetId(patient1);
        createPatientAndGetId(patient2);
        createPatientAndGetId(patient3);

        mockMvc.perform(get("/api/patients/first-name/{firstName}", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnPatientByFirstNameAndDni_whenValidParams() throws Exception {
        createPatientAndGetId(patient1);
        mockMvc.perform(get("/api/patients")
                        .param("firstName", "María")
                        .param("dni", "87654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("María"))
                .andExpect(jsonPath("$[0].dni").value("87654321"));
    }

    @Test
    void shouldReturnNoContent_whenNoPatientsFoundByFirstName() throws Exception {
        mockMvc.perform(get("/api/patients/first-name/{firstName}", "NoExiste"))
                .andExpect(status().isNoContent());
    }

    private Long createPatientAndGetId(PatientRequest patientRequest) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        PatientResponse createdPatient = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                PatientResponse.class
        );
        return createdPatient.getId();
    }
}
