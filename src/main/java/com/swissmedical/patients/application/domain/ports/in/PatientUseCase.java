package com.swissmedical.patients.application.domain.ports.in;

import com.swissmedical.patients.application.domain.model.Patient;

import java.util.List;

public interface PatientUseCase {

  List<Patient> getAll(String name, int page, int size);

  Patient getByDni(String dni);

  List<Patient> getBySocialSecurity(String socialSecurity, int page, int size);

  Patient create(Patient patient);

  Patient update(Long id, Patient patientDetails);

  void delete(Long id);
}
