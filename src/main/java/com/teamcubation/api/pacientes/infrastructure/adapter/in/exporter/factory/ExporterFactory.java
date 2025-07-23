package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.PatientExporter;

public interface ExporterFactory {
    PatientExporter createExporter();
}
