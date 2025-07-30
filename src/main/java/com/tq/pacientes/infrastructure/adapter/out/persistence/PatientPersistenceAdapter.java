package com.tq.pacientes.infrastructure.adapter.out.persistence;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import com.tq.pacientes.infrastructure.adapter.out.persistence.mapper.PatientPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PatientPersistenceAdapter implements PatientRepositoryPort {
    
    private final PatientRepositoryJpa patientRepositoryJpa;

    public PatientPersistenceAdapter(PatientRepositoryJpa patientRepositoryJpa) {
        this.patientRepositoryJpa = patientRepositoryJpa;
    }

    @Override
    public Patient save(Patient patient) {
        var entity = PatientPersistenceMapper.toEntity(patient);
        var saved = patientRepositoryJpa.save(entity);
        return PatientPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Patient> findByDni(String dni) {
        return patientRepositoryJpa.findByDni(dni)
                .map(PatientPersistenceMapper::toDomain);
    }

    @Override
    public List<Patient> findAll() {
        return patientRepositoryJpa.findAll()
                .stream()
                .map(PatientPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Patient update(Patient patient) {
        var entity = PatientPersistenceMapper.toEntity(patient);
        var updated = patientRepositoryJpa.save(entity);
        return PatientPersistenceMapper.toDomain(updated);
    }

    @Override
    public List<Patient> searchByFirstName(String firstName) {
        return patientRepositoryJpa.findByFirstNameContainingIgnoreCase(firstName)
                .stream()
                .map(PatientPersistenceMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Patient> searchByHealthInsurancePaginated(String healthInsurance, int limit, int offset) {
        return patientRepositoryJpa.findByHealthInsurancePaginated(healthInsurance, limit, offset)
                .stream()
                .map(PatientPersistenceMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepositoryJpa.findById(id)
                .map(PatientPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Patient> findByIdIgnoringActive(Long id) {
        return patientRepositoryJpa.findByIdWithoutActiveFilter(id)
                .map(PatientPersistenceMapper::toDomain);
    }

} 