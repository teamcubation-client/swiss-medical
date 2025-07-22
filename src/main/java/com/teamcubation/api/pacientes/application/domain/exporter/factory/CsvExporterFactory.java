package com.teamcubation.api.pacientes.application.domain.exporter.factory;

import com.teamcubation.api.pacientes.application.domain.exporter.impl.CsvPatientExporter;
import com.teamcubation.api.pacientes.application.domain.exporter.PatientExporter;

public class CsvExporterFactory implements ExporterFactory {
    @Override
    public PatientExporter createExporter() {
        return new CsvPatientExporter();
    }
}
