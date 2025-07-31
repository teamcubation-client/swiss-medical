package com.teamcubation.api.pacientes.application.domain.port.out;

import com.teamcubation.api.pacientes.application.domain.model.Patient;

import java.util.List;

public interface PatientExporterPortOut {
    String export(List<Patient> patients);
}
