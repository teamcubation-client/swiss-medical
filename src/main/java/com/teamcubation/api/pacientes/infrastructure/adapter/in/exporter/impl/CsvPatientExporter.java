package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl;

import com.teamcubation.api.pacientes.application.domain.port.out.PatientExporterPortOut;
import com.teamcubation.api.pacientes.application.domain.model.Patient;

import java.util.List;
import java.util.stream.Collectors;

public class CsvPatientExporter implements PatientExporterPortOut {

    @Override
    public String export(List<Patient> patients) {
        String header = "id,nombre,apellido,dni,obra_social,email,telefono\n";

        String body = patients.stream()
                .map(patient -> String.join(",",
                        String.valueOf(patient.getId()),
                        patient.getName(),
                        patient.getLastName(),
                        patient.getDni(),
                        patient.getHealthInsuranceProvider(),
                        patient.getEmail(),
                        patient.getPhoneNumber()))
                .collect(Collectors.joining("\n"));

        return header + body + "\n";
    }
}
