package com.practica.crud_pacientes.infrastructure.adapter.in.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.in.Mediator;
import com.practica.crud_pacientes.application.domain.port.in.PacienteUseCase;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private Mediator mediator;

    private PacienteResponse pacienteResponse;
    private PacienteRequest pacienteRequest;
    private Paciente paciente;
    String endpoint;
    List<Paciente> pacientes;
    Paciente paciente1;
    Paciente paciente2;
    PacienteResponse pacienteResponse1;
    PacienteResponse pacienteResponse2;

    @BeforeEach
    void setUp() {
        this.paciente = new Paciente();
        paciente.setNombre("Jane");
        paciente.setApellido("Doe");
        paciente.setDni("12121212");
        paciente.setEmail("jane@gmail.com");
        paciente.setTelefono("1122334455");
        paciente.setDomicilio("Fake Street 123");
        paciente.setFechaNacimiento(LocalDate.of(2000, 6, 20));
        paciente.setEstadoCivil("Soltera");

        this.pacienteResponse = new PacienteResponse();
        pacienteResponse.setNombre("Jane");
        pacienteResponse.setApellido("Doe");
        pacienteResponse.setDni("12121212");
        pacienteResponse.setObraSocial("Swiss Medical");
        pacienteResponse.setEmail("jane@gmail.com");
        pacienteResponse.setTelefono("1122334455");
        pacienteResponse.setDomicilio("Fake Street 123");
        pacienteResponse.setFechaNacimiento(LocalDate.of(2000, 6, 20));
        pacienteResponse.setEstadoCivil("Soltera");
        this.pacienteRequest = new PacienteRequest();
        pacienteRequest.setNombre("Jane");
        pacienteRequest.setApellido("Doe");
        pacienteRequest.setDni("12121212");
        pacienteRequest.setObraSocial("Swiss Medical");
        pacienteRequest.setEmail("jane@gmail.com");
        pacienteRequest.setTelefono("1122334455");
        pacienteRequest.setDomicilio("Fake Street 123");
        pacienteRequest.setFechaNacimiento(LocalDate.of(2000, 6, 20));
        pacienteRequest.setEstadoCivil("Soltera");
        endpoint = "/pacientes";

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
        doNothing().when(mediator).notifyTraffic(endpoint);
        when(pacienteUseCase.getPacientes()).thenReturn(pacientes);
        when(mapper.domainToResponse(paciente1)).thenReturn(pacienteResponse1);
        when(mapper.domainToResponse(paciente2)).thenReturn(pacienteResponse2);

        mockMvc.perform(get(endpoint))
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

        mockMvc.perform(get(endpoint + "/buscar-por-id/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value(pacienteResponse.getNombre()));
    }

    @Test
    void shouldAddPaciente() throws Exception {
        when(mapper.requestToDomain(pacienteRequest)).thenReturn(paciente);
        when(pacienteUseCase.addPaciente(paciente)).thenReturn(paciente);
        when(mapper.domainToResponse(paciente)).thenReturn(pacienteResponse);

        mockMvc.perform(post(endpoint + "/nuevo-paciente")
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

        mockMvc.perform(put(endpoint + "/actualizar/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pacienteRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeletePaciente() throws Exception {
        int id = 1;
        doNothing().when(pacienteUseCase).deletePaciente(id);

        mockMvc.perform(delete(endpoint + "/eliminar/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetPacienteByDni() throws Exception {
        String dni = "12121212";
        when(pacienteUseCase.getByDni(dni)).thenReturn(paciente);
        when(mapper.domainToResponse(paciente)).thenReturn(pacienteResponse);

        mockMvc.perform(get(endpoint + "/dni/" + dni))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value(paciente.getNombre()));
    }

    @Test
    void shouldGetPacientesByNombre() throws Exception {
        String nombre = "Jane";
        when(pacienteUseCase.getPacientesbyName(nombre)).thenReturn(pacientes);
        when(mapper.domainToResponse(paciente1)).thenReturn(pacienteResponse1);
        when(mapper.domainToResponse(paciente2)).thenReturn(pacienteResponse2);

        mockMvc.perform(get(endpoint + "/nombre/" + nombre))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldGetPacientesByObraSocial() throws Exception {
        String obraSocial = "Swiss Medical";
        int limite = 2;
        int off = 0;

        when(pacienteUseCase.getPacietesbyObraSocial(obraSocial, limite, off)).thenReturn(pacientes);
        when(mapper.domainToResponse(paciente1)).thenReturn(pacienteResponse1);
        when(mapper.domainToResponse(paciente2)).thenReturn(pacienteResponse2);

        mockMvc.perform(get(endpoint + "/obra-social/" + obraSocial)
                .param("limite", String.valueOf(limite))
                .param("off", String.valueOf(off)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }
}
