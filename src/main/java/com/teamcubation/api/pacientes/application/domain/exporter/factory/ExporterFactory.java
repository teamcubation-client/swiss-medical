package com.teamcubation.api.pacientes.application.domain.exporter.factory;

import com.teamcubation.api.pacientes.application.domain.exporter.PatientExporter;

public interface ExporterFactory {
    PatientExporter createExporter();
}
