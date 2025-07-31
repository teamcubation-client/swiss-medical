package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.shared.exception.JsonExportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonPatientExporterTest {
    private Patient patient;
    private Patient patient2;
    private JsonPatientExporter mockedExporter;

    @BeforeEach
    void setUp() {
        this.patient = new Patient();
        patient.setId(1L);
        patient.setName("Ana");
        patient.setLastName("Gómez");
        patient.setDni("12345678");
        patient.setHealthInsuranceProvider("OSDE");
        patient.setEmail("ana@example.com");
        patient.setPhoneNumber("123456789");

        this.patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Carlos");
        patient2.setLastName("Pérez");
        patient2.setDni("87654321");
        patient2.setHealthInsuranceProvider("Swiss Medical");
        patient2.setEmail("carlos@example.com");
        patient2.setPhoneNumber("987654321");

        // Mock configurado para lanzar excepción cuando se invoque export con cualquier lista
        mockedExporter = mock(JsonPatientExporter.class);
        when(mockedExporter.export(anyList()))
                .thenThrow(new JsonExportException("pacientes", new RuntimeException("forced error")));
    }

    @Test
    void export_shouldReturnJsonString() {
        JsonPatientExporter exporter = new JsonPatientExporter();
        List<Patient> patients = List.of(patient, patient2);

        String json = exporter.export(patients);

        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"name\":\"Ana\""));
        assertTrue(json.contains("\"lastName\":\"Gómez\""));
        assertTrue(json.contains("\"dni\":\"12345678\""));
        assertTrue(json.contains("\"healthInsuranceProvider\":\"OSDE\""));

        assertTrue(json.contains("\"id\":2"));
        assertTrue(json.contains("\"name\":\"Carlos\""));
    }

    @Test
    void export_shouldThrowJsonExportException_onJsonProcessingException() {
        Executable executable = () -> mockedExporter.export(List.of());

        JsonExportException thrown = assertThrows(JsonExportException.class, executable);

        assertEquals("forced error", thrown.getCause().getMessage());
    }
}