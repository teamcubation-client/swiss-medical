package com.tq.pacientes.application.domain.port.out;

import com.tq.pacientes.application.domain.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientRepositoryPort {
    Patient save(Patient patient);
    Optional<Patient> findById(Long id);
    Optional<Patient> findByIdIgnoringActive(Long id);
    Optional<Patient> findByDni(String dni);
    List<Patient> findAll();
    Patient update(Patient patient);
    List<Patient> searchByFirstName(String firstName);
    List<Patient> searchByHealthInsurancePaginated(String healthInsurance, int limit, int offset);
} 