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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    private static final String API_PATIENTS_URL = "/api/patients";

    private static final String PATIENT_ID = "1";
    private static final String FIRST_NAME = "Juan";
    private static final String EXISTING_DNI = "12345678";
    private static final String HEALTH_INSURANCE_OSDE = "OSDE";

    private static final String NOT_EXISTING_FIRST_NAME = "NoExiste";

    private static final int PAGE_SIZE = 10;
    private static final int PAGE_OFFSET = 0;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(Long.valueOf(PATIENT_ID))
                .firstName(FIRST_NAME)
                .lastName("Carlos")
                .dni(EXISTING_DNI)
                .healthInsurance(HEALTH_INSURANCE_OSDE)
                .healthPlan("210")
                .address("Calle 123")
                .phoneNumber("1123456789")
                .email("jc@mail.com")
                .active(true)
                .creationDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        patientRequest = PatientRequest.builder()
                .firstName(FIRST_NAME)
                .lastName("Carlos")
                .dni(EXISTING_DNI)
                .healthInsurance(HEALTH_INSURANCE_OSDE)
                .healthPlan("210")
                .address("Calle 123")
                .phoneNumber("1123456789")
                .email("jc@mail.com")
                .build();

        patientResponse = PatientResponse.builder()
                .id(Long.valueOf(PATIENT_ID))
                .firstName(FIRST_NAME)
                .lastName("Carlos")
                .dni(EXISTING_DNI)
                .healthInsurance(HEALTH_INSURANCE_OSDE)
                .healthPlan("210")
                .address("Calle 123")
                .phoneNumber("1123456789")
                .email("jc@mail.com")
                .build();
    }

    @Test
    void shouldReturnCreatedPatient_whenPatientIsCreated() throws Exception {
        when(patientRestMapper.toDomain(patientRequest)).thenReturn(patient);
        when(patientUseCase.create(patient)).thenReturn(patient);
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(post(API_PATIENTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(Long.valueOf(PATIENT_ID)));

        verify(patientUseCase, times(1)).create(patient);
    }

    @Test
    void shouldReturnAllPatients_whenPatientsAreFound() throws Exception {
        when(patientUseCase.getAll()).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get(API_PATIENTS_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(Long.valueOf(PATIENT_ID)));

        verify(patientUseCase, times(1)).getAll();
    }

    @Test
    void shouldReturnPatientByDni_whenExistingDniIsProvided() throws Exception {
        when(patientUseCase.getByDni(EXISTING_DNI)).thenReturn(patient);
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get(API_PATIENTS_URL + "?dni=" + EXISTING_DNI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(Long.valueOf(PATIENT_ID)))
                .andExpect(jsonPath("$[0].dni").value(EXISTING_DNI));

        verify(patientUseCase, times(1)).getByDni(EXISTING_DNI);
    }

    @Test
    void shouldReturnPatientsByFirstName_whenExistingFirstNameIsProvided() throws Exception {
        when(patientUseCase.searchByFirstName(FIRST_NAME)).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get(API_PATIENTS_URL + "?firstName=" + FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME));

        verify(patientUseCase, times(1)).searchByFirstName(FIRST_NAME);
    }

    @Test
    void shouldReturnNoContent_whenPatientsArentFound() throws Exception {
        when(patientUseCase.getAll()).thenReturn(List.of());

        mockMvc.perform(get(API_PATIENTS_URL))
                .andExpect(status().isNoContent());

        verify(patientUseCase, times(1)).getAll();
    }

    @Test
    void shouldReturnPatient_whenPatientIsFoundByDni() throws Exception {
        when(patientUseCase.getByDni(EXISTING_DNI)).thenReturn(patient);
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get(API_PATIENTS_URL + "/dni/{dni}", EXISTING_DNI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(EXISTING_DNI));

        verify(patientUseCase, times(1)).getByDni(EXISTING_DNI);
    }

    @Test
    void shouldReturnPatients_whenPatientIsFoundByFirstName() throws Exception {
        when(patientUseCase.searchByFirstName(FIRST_NAME)).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get(API_PATIENTS_URL + "/first-name/{firstName}", FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(Long.valueOf(PATIENT_ID)))
                .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$[0].dni").value(EXISTING_DNI));

        verify(patientUseCase, times(1)).searchByFirstName(FIRST_NAME);
    }

    @Test
    void shouldReturnNoContent_whenNotExistingFirstNameIsProvided() throws Exception {
        when(patientUseCase.searchByFirstName(NOT_EXISTING_FIRST_NAME)).thenReturn(List.of());

        mockMvc.perform(get(API_PATIENTS_URL + "/first-name/{firstName}", NOT_EXISTING_FIRST_NAME))
                .andExpect(status().isNoContent());

        verify(patientUseCase, times(1)).searchByFirstName(NOT_EXISTING_FIRST_NAME);
    }

    @Test
    void shouldHandleMultipleResults_whenPatientIsFoundByFirstName() throws Exception {
        Patient patient2 = Patient.builder()
                .id(2L)
                .firstName(FIRST_NAME)
                .lastName("Pérez")
                .build();

        PatientResponse response2 = PatientResponse.builder()
                .id(2L)
                .firstName(FIRST_NAME)
                .lastName("Pérez")
                .build();

        when(patientUseCase.searchByFirstName(FIRST_NAME))
                .thenReturn(List.of(patient, patient2));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);
        when(patientRestMapper.toResponse(patient2)).thenReturn(response2);

        mockMvc.perform(get(API_PATIENTS_URL + "/first-name/{firstName}", FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(Long.valueOf(PATIENT_ID)))
                .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value(FIRST_NAME));

        verify(patientUseCase, times(1)).searchByFirstName(FIRST_NAME);
    }

    @Test
    void ShouldHandleEmptyName_whenEmptyNameIsProvided() throws Exception {
        mockMvc.perform(get(API_PATIENTS_URL + "/first-name/ "))
                .andExpect(status().isNoContent());

        verify(patientUseCase, times(1)).searchByFirstName(" ");
    }

    @Test
    void shouldHandleSpecialCharacters_whenPatientIsFoundByFirstName() throws Exception {
        String nameWithSpecialChars = "Juán-Carlos";
        when(patientUseCase.searchByFirstName(nameWithSpecialChars))
                .thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get(API_PATIENTS_URL + "/first-name/{firstName}", nameWithSpecialChars))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME));

        verify(patientUseCase, times(1)).searchByFirstName(nameWithSpecialChars);
    }

    @Test
    void shouldPreserveAllFields_whenPatientIsFoundByFirstName() throws Exception {
        when(patientUseCase.searchByFirstName(FIRST_NAME)).thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get(API_PATIENTS_URL + "/first-name/{firstName}", FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(Long.valueOf(PATIENT_ID)))
                .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$[0].lastName").value("Carlos"))
                .andExpect(jsonPath("$[0].dni").value(EXISTING_DNI))
                .andExpect(jsonPath("$[0].healthInsurance").value(HEALTH_INSURANCE_OSDE))
                .andExpect(jsonPath("$[0].healthPlan").value("210"))
                .andExpect(jsonPath("$[0].address").value("Calle 123"))
                .andExpect(jsonPath("$[0].phoneNumber").value("1123456789"))
                .andExpect(jsonPath("$[0].email").value("jc@mail.com"));

        verify(patientUseCase, times(1)).searchByFirstName(FIRST_NAME);
    }

    @Test
    void shouldReturnPatient_whenPatientIsFoundById() throws Exception {
        when(patientUseCase.getById(Long.valueOf(PATIENT_ID))).thenReturn(Optional.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get(API_PATIENTS_URL + "/" + PATIENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Long.valueOf(PATIENT_ID)));

        verify(patientUseCase, times(1)).getById(Long.valueOf(PATIENT_ID));
    }

    @Test
    void shouldReturnPaginatedPatients_whenHealthInsuranceIsProvided() throws Exception {
        when(patientUseCase.searchByHealthInsurancePaginated(
                HEALTH_INSURANCE_OSDE,
                PAGE_SIZE,
                PAGE_OFFSET
        ))
                .thenReturn(List.of(patient));
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(get(API_PATIENTS_URL + "/health-insurance")
                        .param("healthInsurance", HEALTH_INSURANCE_OSDE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].healthInsurance").value(HEALTH_INSURANCE_OSDE));

        verify(patientUseCase, times(1)).searchByHealthInsurancePaginated(
                HEALTH_INSURANCE_OSDE,
                PAGE_SIZE,
                PAGE_OFFSET
        );
    }

    @Test
    void shouldReturnUpdatedPatient_whenPatientIsUpdated() throws Exception {
        when(patientRestMapper.toDomain(patientRequest)).thenReturn(patient);
        when(patientUseCase.update(eq(Long.valueOf(PATIENT_ID)), any())).thenReturn(patient);
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(patch(API_PATIENTS_URL + "/" + PATIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Long.valueOf(PATIENT_ID)));

        verify(patientUseCase, times(1)).update(eq(Long.valueOf(PATIENT_ID)), any());
    }

    @Test
    void shouldReturnNoContent_whenPatientIsSoftDeleted() throws Exception {
        doNothing().when(patientUseCase).delete(Long.valueOf(PATIENT_ID));

        mockMvc.perform(delete(API_PATIENTS_URL + "/" + PATIENT_ID))
                .andExpect(status().isNoContent());

        verify(patientUseCase, times(1)).delete(Long.valueOf(PATIENT_ID));
    }

    @Test
    void shouldReturnOk_whenPatientIsActivated() throws Exception {
        when(patientRestMapper.toDomain(patientRequest)).thenReturn(patient);
        when(patientUseCase.activate((Long.valueOf(PATIENT_ID)))).thenReturn(patient);
        when(patientRestMapper.toResponse(patient)).thenReturn(patientResponse);

        mockMvc.perform(patch(API_PATIENTS_URL + "/" + PATIENT_ID + "/activate"))
                .andExpect(status().isOk());

        verify(patientUseCase, times(1)).activate((Long.valueOf(PATIENT_ID)));
    }

}