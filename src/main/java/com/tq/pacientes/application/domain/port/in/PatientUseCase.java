package com.tq.pacientes.application.domain.port.in;

import com.tq.pacientes.application.domain.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientUseCase {
    Patient create(Patient patient);
    List<Patient> getAll();
    Optional<Patient> getById(Long id);
    Patient getByDni(String dni);
    List<Patient> searchByFirstName(String firstName);
    List<Patient> searchByHealthInsurancePaginated(String healthInsurance, int limit, int offset);
    Patient update(Long id, Patient patientDetails);
    void delete(Long id);
    Patient activate(Long id);
} 