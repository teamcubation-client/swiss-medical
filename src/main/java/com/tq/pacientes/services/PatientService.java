package com.tq.pacientes.services;

import com.tq.pacientes.models.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient createPatient(Patient patient);
    List<Patient> listAllPatients();
    Optional<Patient> findById(Long id);
    Optional<Patient> findByDni(String dni);
    List<Patient> searchByFirstName(String firstName);
    Patient updatePatient(Long id, Patient patientDetails);
    void deletePatient(Long id);
    void activatePatient(Long id);
}
