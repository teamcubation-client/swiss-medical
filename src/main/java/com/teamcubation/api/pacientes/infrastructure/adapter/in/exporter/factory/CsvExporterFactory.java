package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl.CsvPatientExporter;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.PatientExporter;

public class CsvExporterFactory implements ExporterFactory {
    @Override
    public PatientExporter createExporter() {
        return new CsvPatientExporter();
    }
}
