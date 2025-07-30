package com.tq.pacientes.unit.infrastructure.adapter.in.rest;

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
import static org.mockito.Mockito.*;
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

        verify(patientUseCase, times(1)).create(patient);
    }

    @Test
    void search_ShouldReturnAllPatients() throws Exception {
        when(patientUseCase.getAll()).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(patientUseCase, times(1)).getAll();
    }

    @Test
    void search_ShouldReturnPatientByDni() throws Exception {
        when(patientUseCase.getByDni("12345678")).thenReturn(Optional.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients?dni=12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].dni").value("12345678"));

        verify(patientUseCase, times(1)).getByDni("12345678");
    }

    @Test
    void search_ShouldReturnPatientsByFirstName() throws Exception {
        when(patientUseCase.searchByFirstName("Juan")).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients?firstName=Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Juan"));

        verify(patientUseCase, times(1)).searchByFirstName("Juan");
    }

    @Test
    void search_ShouldReturnNoContent_WhenNoResults() throws Exception {
        when(patientUseCase.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());

        verify(patientUseCase, times(1)).getAll();
    }

    @Test
    void findByDni_ShouldReturnPatient() throws Exception {
        when(patientUseCase.getByDni("12345678")).thenReturn(Optional.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/dni/{dni}", "12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("12345678"));

        verify(patientUseCase, times(1)).getByDni("12345678");
    }


    @Test
    void searchByFirstName_ShouldReturnPatients() throws Exception {
        when(patientUseCase.searchByFirstName("Juan")).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/first-name/{firstName}", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[0].lastName").value("Carlos"))
                .andExpect(jsonPath("$[0].dni").value("12345678"));

        verify(patientUseCase, times(1)).searchByFirstName("Juan");
    }

    @Test
    void searchByFirstName_ShouldReturnNoContent_WhenNoMatches() throws Exception {
        when(patientUseCase.searchByFirstName("NoExiste")).thenReturn(List.of());

        mockMvc.perform(get("/api/patients/first-name/{firstName}", "NoExiste"))
                .andExpect(status().isNoContent());

        verify(patientUseCase, times(1)).searchByFirstName("NoExiste");
    }

    @Test
    void searchByFirstName_ShouldHandleMultipleResults() throws Exception {
        Patient patient2 = Patient.builder()
                .id(2L)
                .firstName("Juan")
                .lastName("Pérez")
                .build();

        PatientResponse response2 = PatientResponse.builder()
                .id(2L)
                .firstName("Juan")
                .lastName("Pérez")
                .build();

        when(patientUseCase.searchByFirstName("Juan"))
                .thenReturn(List.of(patient, patient2));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);
        when(patientRestMapper.toResponse(patient2)).thenReturn(response2);

        mockMvc.perform(get("/api/patients/first-name/{firstName}", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Juan"));

        verify(patientUseCase, times(1)).searchByFirstName("Juan");
    }

    @Test
    void searchByFirstName_ShouldHandleEmptyName() throws Exception {
        mockMvc.perform(get("/api/patients/first-name/ "))
                .andExpect(status().isNoContent());

        verify(patientUseCase, times(1)).searchByFirstName(" ");
    }

    @Test
    void searchByFirstName_ShouldHandleSpecialCharacters() throws Exception {
        String nameWithSpecialChars = "Juán-Carlos";
        when(patientUseCase.searchByFirstName(nameWithSpecialChars))
                .thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/first-name/{firstName}", nameWithSpecialChars))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Juan"));

        verify(patientUseCase, times(1)).searchByFirstName(nameWithSpecialChars);
    }

    @Test
    void searchByFirstName_ShouldPreserveAllFields() throws Exception {
        when(patientUseCase.searchByFirstName("Juan")).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/first-name/{firstName}", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[0].lastName").value("Carlos"))
                .andExpect(jsonPath("$[0].dni").value("12345678"))
                .andExpect(jsonPath("$[0].healthInsurance").value("OSDE"))
                .andExpect(jsonPath("$[0].healthPlan").value("210"))
                .andExpect(jsonPath("$[0].address").value("Calle 123"))
                .andExpect(jsonPath("$[0].phoneNumber").value("1123456789"))
                .andExpect(jsonPath("$[0].email").value("jc@mail.com"));

        verify(patientUseCase, times(1)).searchByFirstName("Juan");
    }

    @Test
    void findById_ShouldReturnPatient() throws Exception {
        when(patientUseCase.getById(1L)).thenReturn(Optional.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(patientUseCase, times(1)).getById(1L);
    }

    @Test
    void getByHealthInsurance_ShouldReturnPaginatedPatients() throws Exception {
        when(patientUseCase.searchByHealthInsurancePaginated("OSDE", 10, 0)).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get("/api/patients/health-insurance")
                        .param("healthInsurance", "OSDE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].healthInsurance").value("OSDE"));

        verify(patientUseCase, times(1)).searchByHealthInsurancePaginated("OSDE", 10, 0);
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

        verify(patientUseCase, times(1)).update(eq(1L), any());
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(patientUseCase).delete(1L);

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());

        verify(patientUseCase, times(1)).delete(1L);
    }

    @Test
    void activate_ShouldReturnOk() throws Exception {
        when(patientRestMapper.toDomain(patientRequest)).thenReturn(patient);
        when(patientUseCase.activate(eq(1L))).thenReturn(patient);
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(patch("/api/patients/1/activate"))
                .andExpect(status().isOk());

        verify(patientUseCase, times(1)).activate(eq(1L));
    }

}