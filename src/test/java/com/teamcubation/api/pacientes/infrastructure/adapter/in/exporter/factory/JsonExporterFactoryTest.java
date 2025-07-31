package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.application.domain.port.out.PatientExporterPortOut;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl.JsonPatientExporter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonExporterFactoryTest {
    @Test
    void createPatientExporter_returnsJsonPatientExporter() {
        JsonExporterFactory factory = new JsonExporterFactory();

        PatientExporterPortOut exporter = factory.createPatientExporter();

        assertNotNull(exporter);
        assertInstanceOf(JsonPatientExporter.class, exporter);
    }
}