package com.teamcubation.api.pacientes.application.domain.port.in;

import com.teamcubation.api.pacientes.application.domain.model.Patient;

import java.util.List;

public interface PatientPortIn {
    Patient create(Patient patient);
    List<Patient> getAll(String dni, String name);
    Patient getById(long id);
    Patient getByDni(String dnii);
    List<Patient> getByName(String name);
    List<Patient> getByHealthInsuranceProvider(
            String healthInsuranceProvider,
            int page,
            int size
    );
    Patient updateById(long id, Patient patient);
    void deleteById(long id);
}
