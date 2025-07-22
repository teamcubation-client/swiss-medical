package com.teamcubation.api.pacientes.application.domain.exporter;

import com.teamcubation.api.pacientes.application.domain.model.Patient;

import java.util.List;

public interface PatientExporter {
    String export(List<Patient> patients);
}
