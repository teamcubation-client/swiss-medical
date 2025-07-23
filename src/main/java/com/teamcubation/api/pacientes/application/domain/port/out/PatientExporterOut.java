package com.teamcubation.api.pacientes.application.domain.port.out;

import com.teamcubation.api.pacientes.application.domain.model.Patient;

import java.util.List;

public interface PatientExporterOut {
    String export(List<Patient> patients);
}
