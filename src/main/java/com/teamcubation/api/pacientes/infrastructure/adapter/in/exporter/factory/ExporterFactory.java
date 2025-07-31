package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.application.domain.port.out.PatientExporterPortOut;

public interface ExporterFactory {
    PatientExporterPortOut createPatientExporter();
}
