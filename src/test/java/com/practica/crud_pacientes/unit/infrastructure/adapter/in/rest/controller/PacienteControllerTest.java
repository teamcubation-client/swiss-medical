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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.practica.crud_pacientes.utils.PacienteTestFactory.*;
import static com.practica.crud_pacientes.utils.TestConstants.ENDPOINT;
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

        pacientes = new ArrayList<>();
        paciente1 = new Paciente();
        paciente2 = new Paciente();
        pacienteResponse1 = new PacienteResponse();
        pacienteResponse2 = new PacienteResponse();
        this.pacientes.add(paciente1);
        this.pacientes.add(paciente2);
    }

    @Test
    void shouldReturnAllPacientes() throws Exception {
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
    void shouldGetPacienteById() throws Exception {
        int id = 1;
        when(mapper.requestToDomain(pacienteRequest)).thenReturn(paciente);
        when(pacienteUseCase.getPacienteById(id)).thenReturn(paciente);
        when(mapper.domainToResponse(paciente)).thenReturn(pacienteResponse);

        mockMvc.perform(get(ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value(pacienteResponse.getNombre()));
    }

    @Test
    void shouldAddPaciente() throws Exception {
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
    void shouldUpdatePaciente() throws Exception {
        int id = 1;
        String email = "jDoe@gmail.com";
        paciente.setEmail(email);
        when(mapper.requestToDomain(pacienteRequest)).thenReturn(paciente);
        when(pacienteUseCase.updatePaciente(id, paciente)).thenReturn(paciente);
        when(mapper.domainToResponse(paciente)).thenReturn(pacienteResponse);

        mockMvc.perform(put(ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pacienteRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeletePaciente() throws Exception {
        int id = 1;
        doNothing().when(pacienteUseCase).deletePaciente(id);

        mockMvc.perform(delete(ENDPOINT + "/" + id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

    }

    @Test
    void shouldGetPacienteByDni() throws Exception {
        String dni = "12121212";
        when(pacienteUseCase.getPacienteByDni(dni)).thenReturn(paciente);
        when(mapper.domainToResponse(paciente)).thenReturn(pacienteResponse);

        mockMvc.perform(get(ENDPOINT + "/dni/" + dni))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value(paciente.getNombre()));
    }

    @Test
    void shouldGetPacientesByNombre() throws Exception {
        String nombre = "Jane";
        when(pacienteUseCase.getPacientesByName(nombre)).thenReturn(pacientes);
        when(mapper.domainToResponse(paciente1)).thenReturn(pacienteResponse1);
        when(mapper.domainToResponse(paciente2)).thenReturn(pacienteResponse2);

        mockMvc.perform(get(ENDPOINT + "/nombre/" + nombre))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldGetPacientesByObraSocial() throws Exception {
        String obraSocial = "Swiss Medical";
        int limite = 2;
        int off = 0;

        when(pacienteUseCase.getPacientesByObraSocial(obraSocial, limite, off)).thenReturn(pacientes);
        when(mapper.domainToResponse(paciente1)).thenReturn(pacienteResponse1);
        when(mapper.domainToResponse(paciente2)).thenReturn(pacienteResponse2);

        mockMvc.perform(get(ENDPOINT + "/obra-social/" + obraSocial)
                .param("limite", String.valueOf(limite))
                .param("off", String.valueOf(off)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }
}
