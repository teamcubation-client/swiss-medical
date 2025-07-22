package com.teamcubation.api.pacientes.infrastructure.adapter.out.export.impl;

import com.teamcubation.api.pacientes.infrastructure.adapter.out.export.PatientExporter;
import com.teamcubation.api.pacientes.application.domain.model.Patient;

import java.util.List;

public class JsonPatientExporter implements PatientExporter {

    @Override
    public String export(List<Patient> patients) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            stringBuilder.append("{")
                    .append("\"id\":").append(p.getId()).append(",")
                    .append("\"name\":\"").append(p.getName()).append("\",")
                    .append("\"lastName\":\"").append(p.getLastName()).append("\",")
                    .append("\"dni\":\"").append(p.getDni()).append("\",")
                    .append("\"healthInsuranceProvider\":\"").append(p.getHealthInsuranceProvider()).append("\",")
                    .append("\"email\":\"").append(p.getEmail()).append("\",")
                    .append("\"phoneNumber\":\"").append(p.getPhoneNumber()).append("\"")
                    .append("}");
            if (i < patients.size() - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
