package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl.JsonPatientExporter;
import com.teamcubation.api.pacientes.application.domain.port.out.PatientExporterOut;

public class JsonExporterFactory implements ExporterFactory {
    @Override
    public PatientExporterOut createPatientExporter() {
        return new JsonPatientExporter();
    }
}