package com.practica.crud_pacientes.unit.infrastructure.adapter.in.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.in.TrafficNotifier;
import com.practica.crud_pacientes.application.domain.port.in.PacienteUseCase;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.controller.PacienteController;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteRequest;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteResponse;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.mapper.PacienteRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.practica.crud_pacientes.utils.PacienteTestFactory.*;
import static com.practica.crud_pacientes.utils.TestConstants.LIMITE;
import static com.practica.crud_pacientes.utils.TestConstants.OBRA_SOCIAL;
import static com.practica.crud_pacientes.utils.TestConstants.NOMBRE;
import static com.practica.crud_pacientes.utils.TestConstants.OFF;
import static com.practica.crud_pacientes.utils.TestConstants.DNI;
import static com.practica.crud_pacientes.utils.TestConstants.ID;
import static com.practica.crud_pacientes.utils.TestConstants.ENDPOINT;
import static com.practica.crud_pacientes.utils.TestConstants.EMAIL;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PacienteController.class)
class PacienteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PacienteUseCase pacienteUseCase;
    @MockitoBean
    private PacienteRestMapper mapper;
    @MockitoBean
    private TrafficNotifier trafficNotifier;

    private PacienteResponse pacienteResponse;
    private PacienteRequest pacienteRequest;
    private Paciente paciente;
    List<Paciente> pacientes;
    Paciente paciente1;
    Paciente paciente2;
    PacienteResponse pacienteResponse1;
    PacienteResponse pacienteResponse2;

    @BeforeEach
    void setUp() {
        paciente = buildDomain();
        pacienteResponse = buildResponse();
        pacienteRequest = buildRequest();

        paciente1 = new Paciente();
        paciente2 = new Paciente();
        pacientes = List.of(paciente1, paciente2);
        pacienteResponse1 = new PacienteResponse();
        pacienteResponse2 = new PacienteResponse();
    }

    @Test
    @DisplayName("Should return all pacientes when GET request is performed")
    void shouldReturnAllPacientes_whenGetRequestIsPerformed() throws Exception {
        doNothing().when(trafficNotifier).notify(ENDPOINT);
        when(pacienteUseCase.getPacientes()).thenReturn(pacientes);
        when(mapper.domainToResponse(paciente1)).thenReturn(pacienteResponse1);
        when(mapper.domainToResponse(paciente2)).thenReturn(pacienteResponse2);

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Should return paciente when GET by ID is performed")
    void shouldReturnPaciente_whenGetByIdIsPerformed() throws Exception {
        when(mapper.requestToDomain(pacienteRequest)).thenReturn(paciente);
        when(pacienteUseCase.getPacienteById(ID)).thenReturn(paciente);
        when(mapper.domainToResponse(paciente)).thenReturn(pacienteResponse);

        mockMvc.perform(get(ENDPOINT + "/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value(pacienteResponse.getNombre()));
    }

    @Test
    @DisplayName("Should add paciente when POST request with valid body")
    void shouldAddPaciente_whenPostRequestWithValidBody() throws Exception {
        when(mapper.requestToDomain(pacienteRequest)).thenReturn(paciente);
        when(pacienteUseCase.addPaciente(paciente)).thenReturn(paciente);
        when(mapper.domainToResponse(paciente)).thenReturn(pacienteResponse);

        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pacienteRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should update paciente when PUT request with valid body")
    void shouldUpdatePaciente_whenPutRequestWithValidBody() throws Exception {
        paciente.setEmail(EMAIL);
        when(mapper.requestToDomain(pacienteRequest)).thenReturn(paciente);
        when(pacienteUseCase.updatePaciente(ID, paciente)).thenReturn(paciente);
        when(mapper.domainToResponse(paciente)).thenReturn(pacienteResponse);

        mockMvc.perform(put(ENDPOINT + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pacienteRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete paciente when DELETE request is performed")
    void shouldDeletePaciente_whenDeleteRequestIsPerformed() throws Exception {
        doNothing().when(pacienteUseCase).deletePaciente(ID);

        mockMvc.perform(delete(ENDPOINT + "/" + ID))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

    }

    @Test
    @DisplayName("Should return paciente when GET by DNI is performed")
    void shouldReturnPaciente_whenGetByDniIsPerformed() throws Exception {
        when(pacienteUseCase.getPacienteByDni(DNI)).thenReturn(paciente);
        when(mapper.domainToResponse(paciente)).thenReturn(pacienteResponse);

        mockMvc.perform(get(ENDPOINT + "/dni/" + DNI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value(paciente.getNombre()));
    }

    @Test
    @DisplayName("Should return pacientes list when GET by nombre is performed")
    void shouldReturnPacientesList_whenGetByNombreIsPerformed() throws Exception {
        when(pacienteUseCase.getPacientesByName(NOMBRE)).thenReturn(pacientes);
        when(mapper.domainToResponse(paciente1)).thenReturn(pacienteResponse1);
        when(mapper.domainToResponse(paciente2)).thenReturn(pacienteResponse2);

        mockMvc.perform(get(ENDPOINT + "/nombre/" + NOMBRE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Should return pacientes list when GET by obra social with pagination")
    void shouldReturnPacientesList_whenGetByObraSocialWithPagination() throws Exception {
        when(pacienteUseCase.getPacientesByObraSocial(OBRA_SOCIAL, LIMITE, OFF)).thenReturn(pacientes);
        when(mapper.domainToResponse(paciente1)).thenReturn(pacienteResponse1);
        when(mapper.domainToResponse(paciente2)).thenReturn(pacienteResponse2);

        mockMvc.perform(get(ENDPOINT + "/obra-social/" + OBRA_SOCIAL)
                .param("limite", String.valueOf(LIMITE))
                .param("off", String.valueOf(OFF)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }
}
