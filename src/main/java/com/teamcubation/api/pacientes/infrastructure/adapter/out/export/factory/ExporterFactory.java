package com.teamcubation.api.pacientes.infrastructure.adapter.out.export.factory;

import com.teamcubation.api.pacientes.infrastructure.adapter.out.export.PatientExporter;

public interface ExporterFactory {
    PatientExporter createExporter();
}
