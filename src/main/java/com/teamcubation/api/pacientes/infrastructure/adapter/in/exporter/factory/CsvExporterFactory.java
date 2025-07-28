package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl.CsvPatientExporter;
import com.teamcubation.api.pacientes.application.domain.port.out.PatientExporterPortOut;

public class CsvExporterFactory implements ExporterFactory {
    @Override
    public PatientExporterPortOut createPatientExporter() {
        return new CsvPatientExporter();
    }
}
