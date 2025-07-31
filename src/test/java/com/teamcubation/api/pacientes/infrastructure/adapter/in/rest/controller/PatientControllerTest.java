package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.application.service.PatientService;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.teamcubation.api.pacientes.shared.TestPatientBuilder;
import com.teamcubation.api.pacientes.shared.exception.ExporterTypeNotSupportedException;
import com.teamcubation.api.pacientes.shared.exception.JsonExportException;
import com.teamcubation.api.pacientes.shared.exception.PatientDniAlreadyInUseException;
import com.teamcubation.api.pacientes.shared.exception.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(PatientController.class)
class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PatientService patientService;
    private ObjectMapper objectMapper;
    private static final String BASE_URL = "/v1/pacientes";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.objectMapper = new ObjectMapper();
    }

    private static class PatientRequestBuilder {
        private String name = "Roberto";
        private String lastName = "Gonzáles";
        private String dni = "35784627";
        private String email;
        private String insurance;
        private String phone;

        PatientControllerTest.PatientRequestBuilder withName() { this.name = null; return this; }
        PatientControllerTest.PatientRequestBuilder withLastName() { this.lastName = null; return this; }
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

    @Nested
    class Create {
        @Test
        void createPatientWithAllFields_ShouldReturn201() throws Exception {
            PatientRequest request = new PatientRequestBuilder()
                    .withEmail("usuario@example.com")
                    .withPhone("+5491144947756")
                    .withInsurance("Swiss Medical")
                    .build();

            Patient pacienteCreado = new TestPatientBuilder()
                    .withEmail(request.getEmail())
                    .withPhone(request.getPhoneNumber())
                    .withInsurance(request.getHealthInsuranceProvider())
                    .build();

            when(patientService.create(any(Patient.class))).thenReturn(pacienteCreado);

            mockMvc.perform(post(BASE_URL)
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

            Patient pacienteCreado = new TestPatientBuilder()
                    .withEmail(null)
                    .withPhone(null)
                    .withInsurance(null)
                    .build();

            when(patientService.create(any(Patient.class))).thenReturn(pacienteCreado);

            mockMvc.perform(post(BASE_URL)
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
                    .withName()
                    .build();

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createPatientWithMissingLastName_ShouldReturn400() throws Exception {
            PatientRequest request = new PatientRequestBuilder()
                    .withLastName()
                    .build();

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"12AB5678", "1267"})
        void createPatientWithInvalidDni_ShouldReturn400(String invalidDni) throws Exception {
            PatientRequest request = new PatientRequestBuilder()
                    .withDni(invalidDni)
                    .build();

            mockMvc.perform(post(BASE_URL)
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

            Patient pacienteCreado = new TestPatientBuilder()
                    .withId(1L)
                    .withEmail(request.getEmail())
                    .build();

            when(patientService.create(any(Patient.class))).thenReturn(pacienteCreado);

            mockMvc.perform(post(BASE_URL)
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

            mockMvc.perform(post(BASE_URL)
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

            Patient pacienteCreado = new TestPatientBuilder()
                    .withId(1L)
                    .withPhone(request.getPhoneNumber())
                    .build();

            when(patientService.create(any(Patient.class))).thenReturn(pacienteCreado);

            mockMvc.perform(post(BASE_URL)
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

            mockMvc.perform(post(BASE_URL)
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

            mockMvc.perform(post(BASE_URL)
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

            Patient pacienteCreado = new TestPatientBuilder()
                    .withId(1L)
                    .withInsurance("Swiss Medical")
                    .build();

            when(patientService.create(any(Patient.class))).thenReturn(pacienteCreado);

            mockMvc.perform(post(BASE_URL)
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
        void createPatientWithExistingDni_ShouldReturn409() throws Exception {
            PatientRequest request = new PatientRequestBuilder()
                    .withDni("35784627")
                    .build();
            long id = 12L;

            when(patientService.create(any(Patient.class)))
                    .thenThrow(new PatientDniAlreadyInUseException(id));

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("El DNI ingresado ya se encuentra en uso en paciente con id: " + id));
        }

        @Test
        void createPatientWithMalformedJson_ShouldReturn400() throws Exception {
            String invalidJson = "{nombre: Maria, apellido: Gomez";

            mockMvc.perform(post(BASE_URL)
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

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: Fallo en base de datos"));
        }
    }

    @Nested
    class Get {
        @Test
        void getAllNoFiltersWithEmptyList_ShouldReturn200WithEmptyList() throws Exception {
            when(patientService.getAll(null, null)).thenReturn(Collections.emptyList());

            mockMvc.perform(get(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isEmpty());
        }

        @Test
        void getAllWithoutFilters_ShouldReturn200WithPatients() throws Exception {
            List<Patient> patients = List.of(
                    new TestPatientBuilder().withId(1L).withName("Juan").withLastName("Perez").withDni("12345678").build(),
                    new TestPatientBuilder().withId(2L).withName("Ana").withLastName("Lopez").withDni("87654321").build()
            );

            when(patientService.getAll(null, null)).thenReturn(patients);

            mockMvc.perform(get(BASE_URL)
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
            Patient patient = new TestPatientBuilder().build();
            List<Patient> filteredPatients = List.of(patient);
            String dniFilter = patient.getDni();
            when(patientService.getAll(dniFilter, null)).thenReturn(filteredPatients);

            mockMvc.perform(get(BASE_URL)
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
            Patient patient = new TestPatientBuilder().build();
            List<Patient> filteredPatients = List.of(patient);
            String nameFilter = patient.getName();

            when(patientService.getAll(null, nameFilter)).thenReturn(filteredPatients);

            mockMvc.perform(get(BASE_URL)
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
            Patient patient = new TestPatientBuilder().build();
            List<Patient> filteredPatients = List.of(patient);
            String dniFilter = patient.getDni();
            String nameFilter = patient.getName();

            when(patientService.getAll(dniFilter, nameFilter)).thenReturn(filteredPatients);

            mockMvc.perform(get(BASE_URL)
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

        @ParameterizedTest
        @ValueSource(strings = {"12AB5678", "123456", ""})
        void getAllWithInvalidDniFilter_ShouldReturn400(String invalidDni) throws Exception {
            mockMvc.perform(get(BASE_URL)
                            .param("dni", invalidDni)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"Juan123", "Pepe@", "Luis-Martin"})
        void getAllWithInvalidNameFilter_ShouldReturn400(String invalidName) throws Exception {
            mockMvc.perform(get(BASE_URL)
                            .param("nombre", invalidName)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getAllWithInvalidNameEmptyFilter_ShouldReturn400() throws Exception {
            String invalidName = "";

            mockMvc.perform(get(BASE_URL)
                            .param("nombre", invalidName)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getAllWithNullNameAndDniFilter_ShouldReturn400() throws Exception {
            mockMvc.perform(get(BASE_URL + "?nombre=&dni=")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getAllWhenServiceThrowsException_ShouldReturn500() throws Exception {
            when(patientService.getAll(any(), any()))
                    .thenThrow(new RuntimeException("Error inesperado"));

            mockMvc.perform(get(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: Error inesperado"));
        }
    }

    @Nested
    class GetById {
        @Test
        void getByIdWithValidId_ShouldReturn200WithPatientData() throws Exception {
            Patient patient = new TestPatientBuilder().withId(1L).build();
            when(patientService.getById(1L)).thenReturn(patient);

            mockMvc.perform(get(BASE_URL + "/1")
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

            mockMvc.perform(get(BASE_URL + "/99")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Paciente con ID: " + 99L + " no encontrado"));
        }

        @Test
        void getByIdWithNegativeId_ShouldReturn400() throws Exception {
            mockMvc.perform(get(BASE_URL + "/-1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(
                            "Datos ingresados no válidos: getById.id: must be greater than or equal to 1")
                    );
        }

        @Test
        void getByIdWithZeroId_ShouldReturn400() throws Exception {
            mockMvc.perform(get(BASE_URL + "/0")
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

            mockMvc.perform(get(BASE_URL + "/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: error"));
        }
    }

    @Nested
    class UpdateById {
        @Test
        void updateByIdWithValidIdAndValidBody_ShouldReturn200WithUpdatedPatient() throws Exception {
            long validId = 1L;
            PatientRequest request = new PatientRequestBuilder()
                    .withEmail("example@gmail.com")
                    .withInsurance("OSDE")
                    .withPhone("+5491148576543").build();
            Patient updatedPatient = new TestPatientBuilder()
                    .withId(validId)
                    .withName(request.getName())
                    .withLastName(request.getLastName())
                    .withDni(request.getDni())
                    .build();

            when(patientService.updateById(eq(validId), any(Patient.class))).thenReturn(updatedPatient);

            mockMvc.perform(put(BASE_URL + "/" + validId)
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

            mockMvc.perform(put(BASE_URL + "/" + invalidId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void updateByIdWithMissingRequiredFields_ShouldReturn400() throws Exception {
            long validId = 1L;
            String invalidJson = "{\"name\": \"\", \"lastName\": \"\", \"dni\": \"\"}";

            mockMvc.perform(put(BASE_URL + "/" + validId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void updateByIdWithDniAlreadyInUse_ShouldReturn409() throws Exception {
            long validId = 1L;
            PatientRequest request = new PatientRequestBuilder().build();

            when(patientService.updateById(eq(validId), any(Patient.class)))
                    .thenThrow(new PatientDniAlreadyInUseException(validId));

            mockMvc.perform(put(BASE_URL + "/" + validId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("El DNI ingresado ya se encuentra en uso en paciente con id: " + validId));
        }

        @Test
        void updateByIdWithNonExistentId_ShouldReturn404() throws Exception {
            long validId = 99L;
            PatientRequest request = new PatientRequestBuilder().build();

            when(patientService.updateById(eq(validId), any(Patient.class)))
                    .thenThrow(new PatientNotFoundException(validId));

            mockMvc.perform(put(BASE_URL + "/" + validId)
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

            mockMvc.perform(put(BASE_URL + "/" + validId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: error"));

            verify(patientService).updateById(eq(validId), any(Patient.class));
        }
    }

    @Nested
    class DeleteById {
        @Test
        void deleteByIdWithValidId_ShouldReturn204() throws Exception {
            long validId = 1L;

            mockMvc.perform(delete(BASE_URL + "/" + validId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteByIdWithInvalidId_ShouldReturn400() throws Exception {
            mockMvc.perform(delete(BASE_URL + "/-1")
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

            mockMvc.perform(delete(BASE_URL + "/" + nonExistentId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Paciente con ID: " + nonExistentId + " no encontrado"));
        }

        @Test
        void deleteByIdWithUnexpectedError_ShouldReturn500() throws Exception {
            long validId = 1L;
            doThrow(new RuntimeException("error")).when(patientService).deleteById(validId);

            mockMvc.perform(delete(BASE_URL + "/" + validId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: error"));
        }
    }

    @Nested
    class GetByDni {
        @Test
        void getByDniWithValidDni_ShouldReturn200WithPatientData() throws Exception {
            Patient patient = new TestPatientBuilder().build();
            String validDni = patient.getDni();
            when(patientService.getByDni(validDni)).thenReturn(patient);

            mockMvc.perform(get(BASE_URL + "/dni/" + validDni)
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

            mockMvc.perform(get(BASE_URL + "/dni/" + nonExistentDni)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Paciente con DNI: " + nonExistentDni +" no encontrado"));
        }

        @Test
        void getByDniWithInvalidLength_ShouldReturn400() throws Exception {
            mockMvc.perform(get(BASE_URL + "/dni/1234")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            mockMvc.perform(get(BASE_URL + "/dni/123456789")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getByDniWithInvalidCharacters_ShouldReturn400() throws Exception {
            mockMvc.perform(get(BASE_URL + "/dni/12A45678")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            mockMvc.perform(get(BASE_URL + "/dni/1234-678")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetByName {
        @Test
        void getByNameWithValidName_ShouldReturn200WithNonEmptyList() throws Exception {
            Patient patient = new TestPatientBuilder().withId(1L).build();
            List<Patient> patients = List.of(patient);
            String name = patient.getName();

            when(patientService.getByName(name)).thenReturn(patients);

            mockMvc.perform(get(BASE_URL + "/nombre/" + name)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].id").value(1))
                    .andExpect(jsonPath("$.data[0].name").value(name))
                    .andExpect(jsonPath("$.data[0].lastName").value(patient.getLastName()))
                    .andExpect(jsonPath("$.data[0].dni").value(patient.getDni()));
        }

        @Test
        void getByNameWithValidName_ShouldReturn200WithEmptyList() throws Exception {
            when(patientService.getByName("nonExistent")).thenReturn(Collections.emptyList());

            mockMvc.perform(get(BASE_URL + "/nombre/nonExistent")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isEmpty());
        }

        @Test
        void getByNameWithInvalidName_ShouldReturn400() throws Exception {
            String invalidName = "Juan123";

            mockMvc.perform(get(BASE_URL + "/nombre/" + invalidName)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getByNameWithUnexpectedError_ShouldReturn500() throws Exception {
            String name = "Ana";
            when(patientService.getByName(name)).thenThrow(new RuntimeException("Error inesperado"));

            mockMvc.perform(get(BASE_URL + "/nombre/" + name)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: Error inesperado"));
        }
    }

    @Nested
    class GetByHealthInsuranceProvider {
        @Test
        void getByHealthInsuranceProviderWithValidProviderAndEmptyList_ShouldReturn200WithEmptyList() throws Exception {
            when(patientService.getByHealthInsuranceProvider("OSDE", 0, 5))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(get(BASE_URL + "/obra-social")
                            .param("obra_social", "OSDE")
                            .param("page", "0")
                            .param("size", "5")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isEmpty());
        }

        @Test
        void getByHealthInsuranceProviderWithValidProviderAndPatients_ShouldReturn200WithPatients() throws Exception {
            String validProvider = "Swiss Medical";
            Patient patient = new TestPatientBuilder().withId(1L).withInsurance(validProvider).build();
            Patient patient2 = new TestPatientBuilder().withId(2L).withName("Juan").withLastName("Perez").withDni("12345678").withInsurance(validProvider).build();
            List<Patient> patients = List.of(patient, patient2);

            when(patientService.getByHealthInsuranceProvider(validProvider, 0, 5))
                    .thenReturn(patients);

            mockMvc.perform(get(BASE_URL + "/obra-social")
                            .param("obra_social", validProvider)
                            .param("page", "0")
                            .param("size", "5")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(patients.size()))
                    .andExpect(jsonPath("$.data[0].id").value(1))
                    .andExpect(jsonPath("$.data[0].name").value(patient.getName()))
                    .andExpect(jsonPath("$.data[0].lastName").value(patient.getLastName()))
                    .andExpect(jsonPath("$.data[0].dni").value(patient.getDni()))
                    .andExpect(jsonPath("$.data[1].id").value(2))
                    .andExpect(jsonPath("$.data[1].name").value(patient2.getName()))
                    .andExpect(jsonPath("$.data[1].lastName").value(patient2.getLastName()))
                    .andExpect(jsonPath("$.data[1].dni").value(patient2.getDni()));
        }

        @Test
        void getByHealthInsuranceProviderWithInvalidProvider_ShouldReturn400() throws Exception {
            mockMvc.perform(get(BASE_URL + "/obra-social")
                            .param("obra_social", "OSDE-123")
                            .param("page", "0")
                            .param("size", "5")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getByHealthInsuranceProviderWithNegativePage_ShouldReturn400() throws Exception {
            mockMvc.perform(get(BASE_URL + "/obra-social")
                            .param("obra_social", "OSDE")
                            .param("page", "-1")
                            .param("size", "5")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getByHealthInsuranceProviderWithTooSmallSize_ShouldReturn400() throws Exception {
            mockMvc.perform(get(BASE_URL + "/obra-social")
                            .param("obra_social", "OSDE")
                            .param("page", "0")
                            .param("size", "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getByHealthInsuranceProviderWithUnexpectedError_ShouldReturn500() throws Exception {
            when(patientService.getByHealthInsuranceProvider("OSDE", 0, 5))
                    .thenThrow(new RuntimeException("error"));

            mockMvc.perform(get(BASE_URL + "/obra-social")
                            .param("obra_social", "OSDE")
                            .param("page", "0")
                            .param("size", "5")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado: error"));
        }
    }

    @Nested
    class Export {
        @Test
        void exportWithValidCsvFormatAndPatients_ShouldReturn200WithCsvContent() throws Exception {
            String csvContent = "id,nombre,apellido,dni,obra_social,email,telefono\n1,Juan,Perez,12345678,OSDE,juan@mail.com,123456789\n";
            when(patientService.exportPatients("csv")).thenReturn(csvContent);

            mockMvc.perform(get(BASE_URL + "/exportar")
                            .param("formato", "csv")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").value(csvContent));
        }

        @Test
        void exportWithValidCsvFormatAndNoPatients_ShouldReturn200WithOnlyHeaders() throws Exception {
            String csvHeaders = "id,nombre,apellido,dni,obra_social,email,telefono\n";
            when(patientService.exportPatients("csv")).thenReturn(csvHeaders);

            mockMvc.perform(get(BASE_URL + "/exportar")
                            .param("formato", "csv")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").value(csvHeaders));
        }

        @Test
        void exportWithValidJsonFormatAndPatients_ShouldReturn200WithJsonArray() throws Exception {
            String jsonContent = "[{\"id\":1,\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"dni\":\"12345678\"}]";
            when(patientService.exportPatients("json")).thenReturn(jsonContent);

            mockMvc.perform(get(BASE_URL + "/exportar")
                            .param("formato", "json")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").value(jsonContent));
        }

        @Test
        void exportWithValidJsonFormatAndNoPatients_ShouldReturn200WithEmptyArray() throws Exception {
            when(patientService.exportPatients("json")).thenReturn("[]");

            mockMvc.perform(get(BASE_URL + "/exportar")
                            .param("formato", "json")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").value("[]"));
        }

        @Test
        void exportWithUnsupportedFormat_ShouldReturn500() throws Exception {
            String unsupportedFormat = "xml";
            when(patientService.exportPatients(unsupportedFormat))
                    .thenThrow(new ExporterTypeNotSupportedException(unsupportedFormat));

            mockMvc.perform(get(BASE_URL + "/exportar")
                            .param("formato", unsupportedFormat)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Tipo de exportación ingresado '" + unsupportedFormat + "' no soportado"));
        }

        @Test
        void exportWithJsonProcessingError_ShouldReturn500() throws Exception {
            when(patientService.exportPatients("json"))
                    .thenThrow(new JsonExportException("pacientes", new RuntimeException("Error interno")));

            mockMvc.perform(get(BASE_URL + "/exportar")
                            .param("formato", "json")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Error al serializar pacientes a JSON"));
        }

        @Test
        void exportWithValidFormatAndEmptyString_ShouldReturn200WithEmptyString() throws Exception {
            when(patientService.exportPatients("csv"))
                    .thenReturn("");

            mockMvc.perform(get(BASE_URL + "/exportar")
                            .param("formato", "csv")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").value(
                                    ""
                            )
                    );
        }
    }

}