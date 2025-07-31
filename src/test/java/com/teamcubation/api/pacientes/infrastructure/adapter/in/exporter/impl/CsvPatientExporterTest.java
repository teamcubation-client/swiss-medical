package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvPatientExporterTest {

    private CsvPatientExporter csvPatientExporter;
    private Patient patient;
    private Patient patient2;

    @BeforeEach
    void setUp() {
        this.csvPatientExporter = new CsvPatientExporter();
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
    }

    @Test
    void export_shouldReturnCsvString() {
        List<Patient> patients = List.of(patient, patient2);

        String csv = this.csvPatientExporter.export(patients);

        String expectedHeader = "id,nombre,apellido,dni,obra_social,email,telefono\n";

        assertTrue(csv.startsWith(expectedHeader));
        assertTrue(csv.contains("1,Ana,Gómez,12345678,OSDE,ana@example.com,123456789"));
        assertTrue(csv.contains("2,Carlos,Pérez,87654321,Swiss Medical,carlos@example.com,987654321"));
        assertTrue(csv.endsWith("\n"));
    }

    @Test
    void export_emptyList_shouldReturnOnlyHeader() {
        String csv = this.csvPatientExporter.export(List.of());

        String expectedHeader = "id,nombre,apellido,dni,obra_social,email,telefono\n";

        assertEquals(expectedHeader + "\n", csv);
    }
}