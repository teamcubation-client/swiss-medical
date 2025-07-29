package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.application.service.PatientService;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.teamcubation.api.pacientes.shared.exception.PatientDniAlreadyInUseException;
import com.teamcubation.api.pacientes.shared.exception.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(PatientController.class)
class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PatientService patientService;
    @InjectMocks
    private PatientController patientController;
    private ObjectMapper objectMapper;
    private String baseURL;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.objectMapper = new ObjectMapper();
        this.baseURL = "/v1/pacientes";
    }

    static class PatientBuilder {
        private Long id;
        private String name = "Roberto";
        private String lastName = "Gonzáles";
        private String dni = "35784627";
        private String insurance = null;
        private String email = null;
        private String phone = null;

        PatientControllerTest.PatientBuilder withId(Long id) { this.id = id; return this; }
        PatientControllerTest.PatientBuilder withName(String name) { this.name = name; return this; }
        PatientControllerTest.PatientBuilder withLastName(String lastName) { this.lastName = lastName; return this; }
        PatientControllerTest.PatientBuilder withDni(String dni) { this.dni = dni; return this; }
        PatientControllerTest.PatientBuilder withInsurance(String insurance) { this.insurance = insurance; return this; }
        PatientControllerTest.PatientBuilder withEmail(String email) { this.email = email; return this; }
        PatientControllerTest.PatientBuilder withPhone(String phone) { this.phone = phone; return this; }

        Patient build() {
            Patient p = new Patient();
            p.setId(id);
            p.setName(name);
            p.setLastName(lastName);
            p.setDni(dni);
            p.setHealthInsuranceProvider(insurance);
            p.setEmail(email);
            p.setPhoneNumber(phone);
            return p;
        }
    }
    static class PatientRequestBuilder {
        private String name = "Roberto";
        private String lastName = "Gonzáles";
        private String dni = "35784627";
        private String email;
        private String insurance;
        private String phone;

        PatientControllerTest.PatientRequestBuilder withName(String name) { this.name = name; return this; }
        PatientControllerTest.PatientRequestBuilder withLastName(String lastName) { this.lastName = lastName; return this; }
        PatientControllerTest.PatientRequestBuilder withDni(String dni) { this.dni = dni; return this; }
        PatientControllerTest.PatientRequestBuilder withInsurance(String insurance) { this.insurance = insurance; return this; }
        PatientControllerTest.PatientRequestBuilder withEmail(String email) { this.email = email; return this; }
        PatientControllerTest.PatientRequestBuilder withPhone(String phone) { this.phone = phone; return this; }

        PatientRequest build() {
            PatientRequest p = new PatientRequest();
            p.setName(name);
            p.setLastName(lastName);
            p.setDni(dni);
            p.setHealthInsuranceProvider(insurance);
            p.setEmail(email);
            p.setPhoneNumber(phone);
            return p;
        }
    }
    static class PatientResponseBuilder {
        private String name = "Roberto";
        private String lastName = "Gonzáles";
        private String dni = "35784627";

        PatientControllerTest.PatientResponseBuilder withName(String name) { this.name = name; return this; }
        PatientControllerTest.PatientResponseBuilder withLastName(String lastName) { this.lastName = lastName; return this; }
        PatientControllerTest.PatientResponseBuilder withDni(String dni) { this.dni = dni; return this; }

        PatientResponse build() {
            PatientResponse p = new PatientResponse();
            p.setName(name);
            p.setLastName(lastName);
            p.setDni(dni);
            return p;
        }
    }

    //TEST CREAR PACIENTE
    @Test
    void createPatientWithAllFields_ShouldReturn201() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withEmail("usuario@example.com")
                .withPhone("+5491144947756")
                .withInsurance("Swiss Medical")
                .build();

        Patient pacienteCreado = new PatientBuilder()
                .withId(1L)
                .withEmail(request.getEmail())
                .withPhone(request.getPhoneNumber())
                .withInsurance(request.getHealthInsuranceProvider())
                .build();

        when(patientService.create(any(Patient.class))).thenReturn(pacienteCreado);

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.lastName").value(request.getLastName()))
                .andExpect(jsonPath("$.data.dni").value(request.getDni()));
    }

    @Test
    void createPatientWithNullOptionalFields_ShouldReturn201() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withEmail(null)
                .withPhone(null)
                .withInsurance(null)
                .build();

        Patient pacienteCreado = new PatientBuilder()
                .withId(1L)
                .withEmail(null)
                .withPhone(null)
                .withInsurance(null)
                .build();

        when(patientService.create(any(Patient.class))).thenReturn(pacienteCreado);

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.lastName").value(request.getLastName()));
    }

    @Test
    void createPatientWithMissingName_ShouldReturn400() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withName(null)
                .build();

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void createPatientWithMissingLastName_ShouldReturn400() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withLastName(null)
                .build();

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatientWithMissingDni_ShouldReturn400() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withDni(null)
                .build();

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatientWithInvalidDniLetters_ShouldReturn400() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withDni("12AB5678")
                .build();

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatientWithInvalidDniLength_ShouldReturn400() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withDni("1267")
                .build();

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatientWithValidEmail_ShouldReturn201() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withEmail("usuario@example.com")
                .build();

        Patient pacienteCreado = new PatientBuilder()
                .withId(1L)
                .withEmail(request.getEmail())
                .build();

        when(patientService.create(any(Patient.class))).thenReturn(pacienteCreado);

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.lastName").value(request.getLastName()));
    }

    @Test
    void createPatientWithInvalidEmail_ShouldReturn400() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withEmail("email-invalido")
                .build();

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatientWithValidPhoneNumber_ShouldReturn201() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withPhone("+5491144947756")
                .build();

        Patient pacienteCreado = new PatientBuilder()
                .withId(1L)
                .withPhone(request.getPhoneNumber())
                .build();

        when(patientService.create(any(Patient.class))).thenReturn(pacienteCreado);

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.lastName").value(request.getLastName()))
                .andExpect(jsonPath("$.data.dni").value(request.getDni()));
    }

    @Test
    void createPatientWithInvalidPhoneNumber_ShouldReturn400() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withPhone("123-abc")
                .build();

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatientWithInvalidPhoneNumberLength_ShouldReturn400() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withPhone("+549114")
                .build();

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPatientWithValidHealthInsuranceProvider_ShouldReturn201() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withInsurance("Swiss Medical")
                .build();

        Patient pacienteCreado = new PatientBuilder()
                .withId(1L)
                .withInsurance("Swiss Medical")
                .build();

        when(patientService.create(any(Patient.class))).thenReturn(pacienteCreado);

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.lastName").value(request.getLastName()))
                .andExpect(jsonPath("$.data.dni").value(request.getDni()));
    }

    @Test
    void createPatientWithExistingDni_ShouldReturn422() throws Exception {
        PatientRequest request = new PatientRequestBuilder()
                .withDni("35784627")
                .build();
        long id = 12L;

        when(patientService.create(any(Patient.class)))
                .thenThrow(new PatientDniAlreadyInUseException(id));

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El DNI ingresado ya se encuentra en uso en paciente con id: " + id));
    }

    @Test
    void createPatientWithMalformedJson_ShouldReturn400() throws Exception {
        String invalidJson = "{nombre: Maria, apellido: Gomez";

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createPatientUnexpectedErrorOccurs_ShouldReturn500() throws Exception {
        PatientRequest request = new PatientRequestBuilder().build();

        when(patientService.create(any(Patient.class)))
                .thenThrow(new RuntimeException("Fallo en base de datos"));

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: Fallo en base de datos"));
    }

    //TEST OBTENER PACIENTES
    @Test
    void getAllNoFiltersWithEmptyList_ShouldReturn200WithEmptyList() throws Exception {
        when(patientService.getAll(null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(baseURL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void getAllWithoutFilters_ShouldReturn200WithPatients() throws Exception {
        List<Patient> patients = List.of(
                new PatientBuilder().withId(1L).withName("Juan").withLastName("Perez").withDni("12345678").build(),
                new PatientBuilder().withId(2L).withName("Ana").withLastName("Lopez").withDni("87654321").build()
        );

        when(patientService.getAll(null, null)).thenReturn(patients);

        mockMvc.perform(get(baseURL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(patients.size()))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Juan"))
                .andExpect(jsonPath("$.data[0].dni").value("12345678"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Ana"))
                .andExpect(jsonPath("$.data[1].dni").value("87654321"));
    }

    @Test
    void getAllWithValidDniFilter_ShouldReturn200WithFilteredList() throws Exception {
        Patient patient = new PatientBuilder().build();
        List<Patient> filteredPatients = List.of(patient);
        String dniFilter = patient.getDni();
        when(patientService.getAll(dniFilter, null)).thenReturn(filteredPatients);

        mockMvc.perform(get(baseURL)
                        .param("dni", dniFilter)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(filteredPatients.size()))
                .andExpect(jsonPath("$.data[0].dni").value(dniFilter));
    }

    @Test
    void getAllWithValidNameFilter_ShouldReturn200WithFilteredList() throws Exception {
        Patient patient = new PatientBuilder().build();
        List<Patient> filteredPatients = List.of(patient);
        String nameFilter = patient.getName();

        when(patientService.getAll(null, nameFilter)).thenReturn(filteredPatients);

        mockMvc.perform(get(baseURL)
                        .param("nombre", nameFilter)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(filteredPatients.size()))
                .andExpect(jsonPath("$.data[0].name").value(nameFilter));
    }

    @Test
    void getAllWithValidDniAndNameFilters_ShouldReturn200WithFilteredList() throws Exception {
        Patient patient = new PatientBuilder().build();
        List<Patient> filteredPatients = List.of(patient);
        String dniFilter = patient.getDni();
        String nameFilter = patient.getName();

        when(patientService.getAll(dniFilter, nameFilter)).thenReturn(filteredPatients);

        mockMvc.perform(get(baseURL)
                        .param("dni", dniFilter)
                        .param("nombre", nameFilter)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(filteredPatients.size()))
                .andExpect(jsonPath("$.data[0].dni").value(dniFilter))
                .andExpect(jsonPath("$.data[0].name").value(nameFilter));
    }

    @Test
    void getAllWithInvalidDniFilter_ShouldReturn400() throws Exception {
        String invalidDni = "12AB5678";

        mockMvc.perform(get(baseURL)
                        .param("dni", invalidDni)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllWithInvalidDniFilterLength_ShouldReturn400() throws Exception {
        String invalidDni = "123456";

        mockMvc.perform(get(baseURL)
                        .param("dni", invalidDni)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllWithInvalidDniEmptyFilter_ShouldReturn400() throws Exception {
        String invalidDni = "";

        mockMvc.perform(get(baseURL)
                        .param("dni", invalidDni)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllWithInvalidNameFilter_ShouldReturn400() throws Exception {
        List<String> invalidNames = List.of("Juan123", "Pepe@", "Luis-Martin");

        for (String invalidName : invalidNames) {
            mockMvc.perform(get(baseURL)
                            .param("nombre", invalidName)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void getAllWithInvalidNameEmptyFilter_ShouldReturn400() throws Exception {
        String invalidName = "";

        mockMvc.perform(get(baseURL)
                        .param("nombre", invalidName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllWithNullNameAndDniFilter_ShouldReturn400() throws Exception {
        mockMvc.perform(get(baseURL + "?nombre=&dni=")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllWhenServiceThrowsException_ShouldReturn500() throws Exception {
        when(patientService.getAll(any(), any()))
                .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(get(baseURL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: Error inesperado"));
    }

    //TEST OBTENER POR ID
    @Test
    void getByIdWithValidId_ShouldReturn200WithPatientData() throws Exception {
        Patient patient = new PatientBuilder().withId(1L).build();
        when(patientService.getById(1L)).thenReturn(patient);

        mockMvc.perform(get(baseURL + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value(patient.getName()))
                .andExpect(jsonPath("$.data.lastName").value(patient.getLastName()))
                .andExpect(jsonPath("$.data.dni").value(patient.getDni()));
        verify(patientService).getById(1L);
    }

    @Test
    void getByIdWithNonExistentId_ShouldReturn404() throws Exception {
        when(patientService.getById(99L)).thenThrow(new PatientNotFoundException(99L));

        mockMvc.perform(get(baseURL + "/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Paciente con ID: " + 99L + " no encontrado"));
    }

    @Test
    void getByIdWithNegativeId_ShouldReturn400() throws Exception {
        mockMvc.perform(get(baseURL + "/-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(
                        "Datos ingresados no válidos: getById.id: must be greater than or equal to 1")
                );
    }

    @Test
    void getByIdWithZeroId_ShouldReturn400() throws Exception {
        mockMvc.perform(get(baseURL + "/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(
                        "Datos ingresados no válidos: getById.id: must be greater than or equal to 1")
                );
    }

    @Test
    void getByIdWithUnexpectedServiceError_ShouldReturn500() throws Exception {
        when(patientService.getById(1L)).thenThrow(new RuntimeException("error"));

        mockMvc.perform(get(baseURL + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: error"));
    }

    //TEST ACTUALIZAR PACIENTE POR ID
    @Test
    void updateByIdWithValidIdAndValidBody_ShouldReturn200WithUpdatedPatient() throws Exception {
        long validId = 1L;
        PatientRequest request = new PatientRequestBuilder()
                .withEmail("example@gmail.com")
                .withInsurance("OSDE")
                .withPhone("+5491148576543").build();
        Patient updatedPatient = new PatientBuilder()
                .withId(validId)
                .withName(request.getName())
                .withLastName(request.getLastName())
                .withDni(request.getDni())
                .build();

        when(patientService.updateById(eq(validId), any(Patient.class))).thenReturn(updatedPatient);

        mockMvc.perform(put(baseURL + "/" + validId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value((int) validId))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.lastName").value(request.getLastName()))
                .andExpect(jsonPath("$.data.dni").value(request.getDni()));
    }

    @Test
    void updateByIdWithInvalidNegativeId_ShouldReturn400() throws Exception {
        long invalidId = -1L;
        PatientRequest request = new PatientRequestBuilder().build();

        mockMvc.perform(put(baseURL + "/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateByIdWithMissingRequiredFields_ShouldReturn400() throws Exception {
        long validId = 1L;
        String invalidJson = "{\"name\": \"\", \"lastName\": \"\", \"dni\": \"\"}";

        mockMvc.perform(put(baseURL + "/" + validId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateByIdWithDniAlreadyInUse_ShouldReturn422() throws Exception {
        long validId = 1L;
        PatientRequest request = new PatientRequestBuilder().build();

        when(patientService.updateById(eq(validId), any(Patient.class)))
                .thenThrow(new PatientDniAlreadyInUseException(validId));

        mockMvc.perform(put(baseURL + "/" + validId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El DNI ingresado ya se encuentra en uso en paciente con id: " + validId));
    }

    @Test
    void updateByIdWithNonExistentId_ShouldReturn404() throws Exception {
        long validId = 99L;
        PatientRequest request = new PatientRequestBuilder().build();

        when(patientService.updateById(eq(validId), any(Patient.class)))
                .thenThrow(new PatientNotFoundException(validId));

        mockMvc.perform(put(baseURL + "/" + validId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Paciente con ID: " + validId + " no encontrado"));
    }

    @Test
    void updateByIdWithUnexpectedError_ShouldReturn500() throws Exception {
        long validId = 1L;
        PatientRequest request = new PatientRequestBuilder().build();

        when(patientService.updateById(eq(validId), any(Patient.class)))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(put(baseURL + "/" + validId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: error"));

        verify(patientService).updateById(eq(validId), any(Patient.class));
    }

    // TEST BORRAR PACIENTE POR ID
    @Test
    void deleteByIdWithValidId_ShouldReturn204() throws Exception {
        long validId = 1L;

        mockMvc.perform(delete(baseURL + "/" + validId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteByIdWithInvalidId_ShouldReturn400() throws Exception {
        mockMvc.perform(delete(baseURL + "/-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(
                        "Datos ingresados no válidos: deleteById.id: must be greater than or equal to 1"
                        )
                );
    }

    @Test
    void deleteByIdWithNonExistentId_ShouldReturn404() throws Exception {
        long nonExistentId = 99L;
        doThrow(new PatientNotFoundException(nonExistentId)).when(patientService).deleteById(nonExistentId);

        mockMvc.perform(delete(baseURL + "/" + nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Paciente con ID: " + nonExistentId + " no encontrado"));
    }

    @Test
    void deleteByIdWithUnexpectedError_ShouldReturn500() throws Exception {
        long validId = 1L;
        doThrow(new RuntimeException("error")).when(patientService).deleteById(validId);

        mockMvc.perform(delete(baseURL + "/" + validId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: error"));
    }

    //TEST OBTENER PACIENTE POR DNI
    @Test
    void getByDniWithValidDni_ShouldReturn200WithPatientData() throws Exception {
        Patient patient = new PatientBuilder().build();
        String validDni = patient.getDni();
        when(patientService.getByDni(validDni)).thenReturn(patient);

        mockMvc.perform(get(baseURL + "/dni/" + validDni)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.dni").value(validDni))
                .andExpect(jsonPath("$.data.name").value(patient.getName()))
                .andExpect(jsonPath("$.data.lastName").value(patient.getLastName()));
    }

    @Test
    void getByDniWithNonExistentDni_ShouldReturn404() throws Exception {
        String nonExistentDni = "87654321";
        when(patientService.getByDni(nonExistentDni)).thenThrow(new PatientNotFoundException(nonExistentDni));

        mockMvc.perform(get(baseURL + "/dni/" + nonExistentDni)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Paciente con DNI: " + nonExistentDni +" no encontrado"));
    }

    @Test
    void getByDniWithInvalidLength_ShouldReturn400() throws Exception {
        mockMvc.perform(get(baseURL + "/dni/1234")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(baseURL + "/dni/123456789")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByDniWithInvalidCharacters_ShouldReturn400() throws Exception {
        mockMvc.perform(get(baseURL + "/dni/12A45678")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(baseURL + "/dni/1234-678")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //TEST OBTENER PACIENTE POR NOMBRE
    @Test
    void getByNameWithValidName_ShouldReturn200WithNonEmptyList() throws Exception {
        Patient patient = new PatientBuilder().withName("Ana").build();
        List<Patient> patients = List.of(patient);

        when(patientService.getByName("Ana")).thenReturn(patients);

        mockMvc.perform(get(baseURL + "/nombre/Ana")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(patients.size()))
                .andExpect(jsonPath("$.data[0].name").value("Ana"));
    }

    @Test
    void getByNameWithValidName_ShouldReturn200WithEmptyList() throws Exception {
        when(patientService.getByName("NoExiste")).thenReturn(Collections.emptyList());

        mockMvc.perform(get(baseURL + "/nombre/NoExiste")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void getByNameWithInvalidName_ShouldReturn400() throws Exception {
        String invalidName = "Juan123";

        mockMvc.perform(get(baseURL + "/nombre/" + invalidName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByNameWithUnexpectedError_ShouldReturn500() throws Exception {
        String name = "Ana";
        when(patientService.getByName(name)).thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(get(baseURL + "/nombre/" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: Error inesperado"));
    }

}