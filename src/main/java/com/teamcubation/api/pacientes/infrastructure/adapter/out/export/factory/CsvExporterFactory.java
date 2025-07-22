package com.teamcubation.api.pacientes.infrastructure.adapter.out.export.factory;

import com.teamcubation.api.pacientes.infrastructure.adapter.out.export.impl.CsvPatientExporter;
import com.teamcubation.api.pacientes.infrastructure.adapter.out.export.PatientExporter;

public class CsvExporterFactory implements ExporterFactory {
    @Override
    public PatientExporter createExporter() {
        return new CsvPatientExporter();
    }
}
