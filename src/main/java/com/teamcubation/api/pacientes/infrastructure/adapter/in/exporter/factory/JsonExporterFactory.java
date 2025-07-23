package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl.JsonPatientExporter;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.PatientExporter;

public class JsonExporterFactory implements ExporterFactory {
    @Override
    public PatientExporter createExporter() {
        return new JsonPatientExporter();
    }
}