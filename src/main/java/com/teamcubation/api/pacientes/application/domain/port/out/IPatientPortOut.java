package com.teamcubation.api.pacientes.application.domain.port.out;

import com.teamcubation.api.pacientes.application.domain.model.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientPortOut {
    Patient save(Patient patient);
    List<Patient> findAll(String dni, String name);
    Optional<Patient> findById(Long id);
    Patient updateById(Patient patient);
    void deleteById(long id);
    Optional<Patient> findByDni(String dni);
    List<Patient> findByName(String name);
    List<Patient> findByHealthInsuranceProvider(
            String healthInsuranceProvider,
            int limit,
            int offset
    );
}
