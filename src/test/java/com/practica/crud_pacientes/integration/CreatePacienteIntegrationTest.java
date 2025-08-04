package com.practica.crud_pacientes.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.out.PacienteRepositoryPort;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.practica.crud_pacientes.utils.PacienteTestFactory.buildRequest;
import static com.practica.crud_pacientes.utils.TestConstants.DNI;
import static com.practica.crud_pacientes.utils.TestConstants.ENDPOINT;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CreatePacienteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private PacienteRepositoryPort pacienteRepositoryPort;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should return 201 when paciente is created successfully")
    void shouldReturn201_whenPacienteIsCreatedSuccessfully() throws Exception {
        PacienteRequest pacienteRequest = buildRequest();

        doReturn(null).when(pacienteRepositoryPort).getPacienteByDni(DNI);


        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pacienteRequest)))
                .andExpect(status().isCreated());

        verify(pacienteRepositoryPort).getPacienteByDni(DNI);
        verify(pacienteRepositoryPort).save(any(Paciente.class));
    }
}
