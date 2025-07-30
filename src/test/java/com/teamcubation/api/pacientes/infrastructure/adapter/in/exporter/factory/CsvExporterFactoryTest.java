package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.application.domain.port.out.PatientExporterPortOut;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl.CsvPatientExporter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvExporterFactoryTest {

    @Test
    void createPatientExporter_returnsCsvPatientExporter() {
        CsvExporterFactory factory = new CsvExporterFactory();

        PatientExporterPortOut exporter = factory.createPatientExporter();

        assertNotNull(exporter);
        assertTrue(exporter instanceof CsvPatientExporter);
    }
}