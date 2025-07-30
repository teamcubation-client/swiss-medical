package com.tq.pacientes.unit.infrastructure.adapter.in.rest.mapper;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.tq.pacientes.infrastructure.adapter.in.rest.mapper.PatientRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PatientRestMapperTest {

    private PatientRestMapper mapper;
    private PatientRequest request;
    private Patient patient;
    private PatientRequest emptyRequest;

    @BeforeEach
    void setUp() {
        mapper = new PatientRestMapper();

        request = PatientRequest.builder()
                .firstName("Juan")
                .lastName("Carlos")
                .dni("12345678")
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance("OSDE")
                .healthPlan("210")
                .address("Calle 123")
                .phoneNumber("1123456789")
                .email("jc@mail.com")
                .build();

        emptyRequest = PatientRequest.builder()
                .build();

        patient = Patient.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Carlos")
                .dni("12345678")
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance("OSDE")
                .healthPlan("210")
                .address("Calle 123")
                .phoneNumber("1123456789")
                .email("jc@mail.com")
                .active(true)
                .creationDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    @Test
    void toDomain_ShouldMapAllFields() {
        Patient result = mapper.toDomain(request);

        assertAll(
                () -> assertNull(result.getId()),
                () -> assertEquals(request.getFirstName(), result.getFirstName()),
                () -> assertEquals(request.getLastName(), result.getLastName()),
                () -> assertEquals(request.getDni(), result.getDni()),
                () -> assertEquals(request.getBirthDate(), result.getBirthDate()),
                () -> assertEquals(request.getHealthInsurance(), result.getHealthInsurance()),
                () -> assertEquals(request.getHealthPlan(), result.getHealthPlan()),
                () -> assertEquals(request.getAddress(), result.getAddress()),
                () -> assertEquals(request.getPhoneNumber(), result.getPhoneNumber()),
                () -> assertEquals(request.getEmail(), result.getEmail()),
                () -> assertTrue(result.getActive()),
                () -> assertNotNull(result.getCreationDate()),
                () -> assertNotNull(result.getLastModifiedDate())
        );
    }

    @Test
    void toDomain_ShouldHandleNullValues() {

        Patient result = mapper.toDomain(emptyRequest);

        assertAll(
                () -> assertNull(result.getId()),
                () -> assertNull(result.getFirstName()),
                () -> assertNull(result.getLastName()),
                () -> assertNull(result.getDni()),
                () -> assertNull(result.getBirthDate()),
                () -> assertNull(result.getHealthInsurance()),
                () -> assertNull(result.getHealthPlan()),
                () -> assertNull(result.getAddress()),
                () -> assertNull(result.getPhoneNumber()),
                () -> assertNull(result.getEmail()),
                () -> assertTrue(result.getActive()),
                () -> assertNotNull(result.getCreationDate()),
                () -> assertNotNull(result.getLastModifiedDate())
        );
    }

    @Test
    void toResponse_ShouldMapAllFields() {
        PatientResponse result = mapper.toResponse(patient);

        assertAll(
                () -> assertEquals(patient.getId(), result.getId()),
                () -> assertEquals(patient.getFirstName(), result.getFirstName()),
                () -> assertEquals(patient.getLastName(), result.getLastName()),
                () -> assertEquals(patient.getDni(), result.getDni()),
                () -> assertEquals(patient.getBirthDate(), result.getBirthDate()),
                () -> assertEquals(patient.getHealthInsurance(), result.getHealthInsurance()),
                () -> assertEquals(patient.getHealthPlan(), result.getHealthPlan()),
                () -> assertEquals(patient.getAddress(), result.getAddress()),
                () -> assertEquals(patient.getPhoneNumber(), result.getPhoneNumber()),
                () -> assertEquals(patient.getEmail(), result.getEmail()),
                () -> assertEquals(patient.getActive(), result.getActive())
        );
    }

    @Test
    void toResponse_ShouldHandleNullValues() {
        Patient emptyPatient = new Patient();

        PatientResponse result = mapper.toResponse(emptyPatient);

        assertAll(
                () -> assertNull(result.getId()),
                () -> assertNull(result.getFirstName()),
                () -> assertNull(result.getLastName()),
                () -> assertNull(result.getDni()),
                () -> assertNull(result.getBirthDate()),
                () -> assertNull(result.getHealthInsurance()),
                () -> assertNull(result.getHealthPlan()),
                () -> assertNull(result.getAddress()),
                () -> assertNull(result.getPhoneNumber()),
                () -> assertNull(result.getEmail()),
                () -> assertNull(result.getActive())
        );
    }
}
