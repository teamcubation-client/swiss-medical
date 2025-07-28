package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcubation.api.pacientes.application.domain.port.out.PatientExporterPortOut;
import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.shared.exception.JsonExportException;

import java.util.List;

public class JsonPatientExporter implements PatientExporterPortOut {

    private static final String JSON_EXPORT_TARGET_NAME = "pacientes";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String export(List<Patient> patients) {
        try {
            return objectMapper.writeValueAsString(patients);
        } catch (JsonProcessingException e) {
            throw new JsonExportException(JSON_EXPORT_TARGET_NAME, e);
        }
    }
}
