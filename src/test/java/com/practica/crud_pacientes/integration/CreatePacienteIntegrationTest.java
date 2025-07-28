package com.practica.crud_pacientes.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practica.crud_pacientes.application.domain.port.out.PacienteRepositoryPort;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CreatePacienteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PacienteRepositoryPort pacienteRepositoryPort;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    @Rollback
    void shouldCreatePacienteSuccessfully() throws Exception {
        PacienteRequest pacienteRequest = new PacienteRequest();
        pacienteRequest.setNombre("Jane");
        pacienteRequest.setApellido("Doe");
        pacienteRequest.setDni("12121212");
        pacienteRequest.setEmail("jane@gmail.com");
        pacienteRequest.setObraSocial("Swiss Medical");
        pacienteRequest.setTelefono("1122334455");
        pacienteRequest.setDomicilio("Fake Street 123");
        pacienteRequest.setFechaNacimiento(LocalDate.of(2000, 6, 20));
        pacienteRequest.setEstadoCivil("Soltera");

        mockMvc.perform(post("/pacientes/nuevo-paciente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pacienteRequest)))
                .andExpect(status().isCreated());
    }
}
