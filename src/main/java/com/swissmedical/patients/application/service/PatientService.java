package com.swissmedical.patients.application.service;

import com.swissmedical.patients.application.domain.command.CreatePatientCommand;
import com.swissmedical.patients.application.domain.command.UpdatePatientCommand;
import com.swissmedical.patients.application.domain.command.mapper.CreatePatientMapper;
import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.domain.ports.in.*;
import com.swissmedical.patients.application.domain.ports.out.PatientRepositoryPort;
import com.swissmedical.patients.application.domain.ports.out.PatientUpdater;
import com.swissmedical.patients.shared.exceptions.PatientDuplicateException;
import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;
import com.swissmedical.patients.shared.utils.ErrorMessages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientService implements ReadPatientUseCase, CreatePatientUseCase, UpdatePatientUseCase, DeletePatientUseCase {

  private final PatientRepositoryPort patientRepository;
  private final CreatePatientMapper createPatientMapper;
  private final PatientUpdater patientUpdater;

  @Override
  public List<Patient> getAll(String name, int page, int size) {
    if (page <= 0 || size < 0) {
      throw new IllegalArgumentException(ErrorMessages.LIMIT_OFFSET_INVALID);
    }

    if (name.isEmpty()) {
      return patientRepository.findAll(size, (page - 1) * size);
    }

    List<Patient> patients = patientRepository.findByFirstName(name);

    if (patients.isEmpty()) {
      throw new PatientNotFoundException(String.format(ErrorMessages.PATIENT_NOT_FOUND_BY_NAME, name));
    }

    return patients;
  }

  @Override
  public Patient getByDni(String dni) {
    return patientRepository.findByDni(dni)
            .orElseThrow(() -> new PatientNotFoundException(String.format(ErrorMessages.PATIENT_NOT_FOUND_BY_DNI, dni)));
  }

  @Override
  public List<Patient> getBySocialSecurity(String socialSecurity, int page, int size) {
    List<Patient> patients = patientRepository.findBySocialSecurity(socialSecurity, size, (page - 1) * size);

    if (patients.isEmpty()) {
      throw new PatientNotFoundException(String.format(ErrorMessages.PATIENT_NOT_FOUND_BY_SOCIAL_SECURITY, socialSecurity));
    }

    return patients;
  }

  @Override
  public Patient create(CreatePatientCommand command) {
    Patient patient = createPatientMapper.toPatient(command);
    if (patientRepository.existsByDni(patient.getDni())) {
      throw new PatientDuplicateException(String.format(ErrorMessages.PATIENT_DNI_DUPLICATE, patient.getDni()));
    }

    if (patientRepository.existsByEmail(patient.getEmail())) {
      throw new PatientDuplicateException(String.format(ErrorMessages.PATIENT_EMAIL_DUPLICATE, patient.getEmail()));
    }

    return patientRepository.save(patient);
  }

  @Override
  public Patient update(Long id, UpdatePatientCommand updatePacienteCommand) {
    Patient existingPatient = patientRepository.findById(id)
            .orElseThrow(() -> new PatientNotFoundException(String.format(ErrorMessages.PATIENT_NOT_FOUND_BY_ID, id)));

    patientUpdater.update(updatePacienteCommand, existingPatient);

    return patientRepository.save(existingPatient);
  }

  @Override
  public void delete(Long id) {
    if (!patientRepository.existsById(id)) {
      throw new PatientNotFoundException(String.format(ErrorMessages.PATIENT_NOT_FOUND_BY_ID, id));
    }
    patientRepository.delete(id);
  }
}
