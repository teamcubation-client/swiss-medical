package com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.domain.ports.out.PatientRepositoryPort;
import com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.entity.PatientEntity;
import com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.mapper.PatientEntityMapper;
import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepositoryAdapter implements PatientRepositoryPort {

  private final PatientJpaRepository patientJpaRepository;

  public PatientRepositoryAdapter(PatientJpaRepository patientJpaRepository) {
    this.patientJpaRepository = patientJpaRepository;
  }

  @Override
  public Patient save(Patient patient) {
    PatientEntity savedEntity = patientJpaRepository.save(PatientEntityMapper.toEntity(patient));
    return PatientEntityMapper.toDomain(savedEntity);
  }

  @Override
  public Patient update(Long id, Patient patient) {
    if (!patientJpaRepository.existsById(id)) {
      throw new PatientNotFoundException("Patient with ID " + id + " does not exist.");
    }
    patient.setId(id);
    PatientEntity updatedEntity = patientJpaRepository.save(PatientEntityMapper.toEntity(patient));
    return PatientEntityMapper.toDomain(updatedEntity);
  }

  @Override
  public void delete(Long id) {
    if (!patientJpaRepository.existsById(id)) {
      throw new PatientNotFoundException("Patient with ID " + id + " does not exist.");
    }
    patientJpaRepository.deleteById(id);
  }

  @Override
  public boolean existsByDni(String dni) {
    return patientJpaRepository.existsByDni(dni);
  }

  @Override
  public boolean existsByEmail(String email) {
    return patientJpaRepository.existsByEmail(email);
  }

  @Override
  public boolean existsById(Long id) {
    return patientJpaRepository.existsById(id);
  }

  @Override
  public List<Patient> findAll(int limit, int offset) {
    return patientJpaRepository.findAll(limit, offset)
            .stream()
            .map(PatientEntityMapper::toDomain)
            .toList();
  }

  @Override
  public Optional<Patient> findByDni(String dni) {
    return patientJpaRepository.findByDni(dni)
            .map(PatientEntityMapper::toDomain);
  }

  @Override
  public Optional<Patient> findById(Long id) {
    return patientJpaRepository.findById(id)
            .map(PatientEntityMapper::toDomain);
  }

  @Override
  public List<Patient> findByFirstName(String firstName) {
    return patientJpaRepository.findByFirstName(firstName)
            .stream()
            .map(PatientEntityMapper::toDomain)
            .toList();
  }

  @Override
  public List<Patient> findBySocialSecurity(String socialSecurity, int limit, int offset) {
    return patientJpaRepository.findBySocialSecurity(socialSecurity, limit, offset)
            .stream()
            .map(PatientEntityMapper::toDomain)
            .toList();
  }
}
