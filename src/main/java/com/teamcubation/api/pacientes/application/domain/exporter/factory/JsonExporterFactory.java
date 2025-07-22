package com.teamcubation.api.pacientes.application.domain.exporter.factory;

import com.teamcubation.api.pacientes.application.domain.exporter.impl.JsonPatientExporter;
import com.teamcubation.api.pacientes.application.domain.exporter.PatientExporter;

public class JsonExporterFactory implements ExporterFactory {
    @Override
    public PatientExporter createExporter() {
        return new JsonPatientExporter();
    }
}