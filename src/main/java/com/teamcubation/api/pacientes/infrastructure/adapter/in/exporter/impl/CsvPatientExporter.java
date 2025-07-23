package com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.impl;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.PatientExporter;
import com.teamcubation.api.pacientes.application.domain.model.Patient;

import java.util.List;

public class CsvPatientExporter implements PatientExporter {

    @Override
    public String export(List<Patient> patients) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id,name,lastName,dni,healthInsuranceProvider,email,phoneNumber\n");
        for (Patient p : patients) {
            stringBuilder.append(p.getId()).append(",")
                    .append(p.getName()).append(",")
                    .append(p.getLastName()).append(",")
                    .append(p.getDni()).append(",")
                    .append(p.getHealthInsuranceProvider()).append(",")
                    .append(p.getEmail()).append(",")
                    .append(p.getPhoneNumber()).append("\n");
        }
        return stringBuilder.toString();
    }
}
