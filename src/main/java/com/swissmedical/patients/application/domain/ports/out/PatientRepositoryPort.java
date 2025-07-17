package com.swissmedical.patients.application.domain.ports.out;

import com.swissmedical.patients.application.domain.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepositoryPort {

  Patient save(Patient patient);

  Patient update(Long id, Patient patient);

  void delete(Long id);

  boolean existsByDni(String dni);

  boolean existsByEmail(String email);

  boolean existsById(Long id);

  List<Patient> findAll(int limit, int offset);

  Optional<Patient> findByDni(String dni);

  Optional<Patient> findById(Long id);

  List<Patient> findByFirstName(String firstName);

   List<Patient> findBySocialSecurity(String socialSecurity, int limit, int offset);
}
