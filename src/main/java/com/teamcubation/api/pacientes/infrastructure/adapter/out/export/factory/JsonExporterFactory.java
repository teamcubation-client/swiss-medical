package com.teamcubation.api.pacientes.infrastructure.adapter.out.export.factory;

import com.teamcubation.api.pacientes.infrastructure.adapter.out.export.impl.JsonPatientExporter;
import com.teamcubation.api.pacientes.infrastructure.adapter.out.export.PatientExporter;

public class JsonExporterFactory implements ExporterFactory {
    @Override
    public PatientExporter createExporter() {
        return new JsonPatientExporter();
    }
}