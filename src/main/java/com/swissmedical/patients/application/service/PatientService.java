package com.swissmedical.patients.application.service;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.domain.ports.in.PatientUseCase;
import com.swissmedical.patients.application.domain.ports.out.PatientRepository;
import com.swissmedical.patients.shared.exceptions.PatientDuplicateException;
import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService implements PatientUseCase {

  private final PatientRepository patientRepository;

  public PatientService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  @Override
  public List<Patient> getAll(String name, int page, int size) {
    if (page <= 0 || size < 0) {
      throw new IllegalArgumentException("Limit must be greater than 0 and offset must be non-negative.");
    }

    if (name.isEmpty()) {
      return patientRepository.findAll(size, (page - 1) * size);
    }

    List<Patient> patients = patientRepository.findByFirstName(name);

    if (patients.isEmpty()) {
      throw new PatientNotFoundException("No patients found with name " + name + ".");
    }

    return patients;
  }

  @Override
  public Patient getByDni(String dni) {
    return patientRepository.findByDni(dni)
            .orElseThrow(() -> new PatientNotFoundException("Patient with DNI " + dni + " does not exist."));
  }

  @Override
  public List<Patient> getBySocialSecurity(String socialSecurity, int page, int size) {
    List<Patient> patients = patientRepository.findBySocialSecurity(socialSecurity, size, (page - 1) * size);

    if (patients.isEmpty()) {
      throw new PatientNotFoundException("No patients found with social security " + socialSecurity + ".");
    }

    return patients;
  }

  @Override
  public Patient create(Patient patient) {
    if (patientRepository.existsByDni(patient.getDni())) {
      throw new PatientDuplicateException("Patient with DNI " + patient.getDni() + " already exists.");
    }

    if (patientRepository.existsByEmail(patient.getEmail())) {
      throw new PatientDuplicateException("Patient with email " + patient.getEmail() + " already exists.");
    }

    return patientRepository.save(patient);
  }

  @Override
  public Patient update(Long id, Patient patientDetails) {
    Patient existingPatient = patientRepository.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Patient with ID " + id + " does not exist."));

    existingPatient.setFirstName(patientDetails.getFirstName());
    existingPatient.setLastName(patientDetails.getLastName());
    existingPatient.setEmail(patientDetails.getEmail());
    existingPatient.setPhoneNumber(patientDetails.getPhoneNumber());
    existingPatient.setDni(patientDetails.getDni());
    existingPatient.setSocialSecurity(patientDetails.getSocialSecurity());

    return patientRepository.save(existingPatient);
  }

  @Override
  public void delete(Long id) {
    if (!patientRepository.existsById(id)) {
      throw new PatientNotFoundException("Patient with ID " + id + " does not exist.");
    }
    patientRepository.delete(id);
  }
}
