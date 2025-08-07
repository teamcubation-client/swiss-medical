package com.tq.pacientes.infrastructure.adapter.out.persistence;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import com.tq.pacientes.infrastructure.adapter.out.persistence.mapper.PatientPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        return toDomain(patientRepositoryJpa.findByDni(dni));
    }

    @Override
    public List<Patient> findAll() {
        return toDomainList(patientRepositoryJpa.findAll());
    }

    @Override
    public Patient update(Patient patient) {
        var entity = PatientPersistenceMapper.toEntity(patient);
        var updated = patientRepositoryJpa.save(entity);
        return PatientPersistenceMapper.toDomain(updated);
    }

    @Override
    public List<Patient> searchByFirstName(String firstName) {
        return toDomainList(patientRepositoryJpa.findByFirstNameContainingIgnoreCase(firstName));
    }

    @Override
    public List<Patient> searchByHealthInsurancePaginated(String healthInsurance, int limit, int offset) {
        return toDomainList(patientRepositoryJpa.findByHealthInsurancePaginated(healthInsurance, limit, offset));
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return toDomain(patientRepositoryJpa.findById(id));
    }

    @Override
    public Optional<Patient> findByIdIgnoringActive(Long id) {
        return toDomain(patientRepositoryJpa.findByIdWithoutActiveFilter(id));
    }

    private List<Patient> toDomainList(List<PatientEntity> entities) {
        return entities.stream()
                .map(PatientPersistenceMapper::toDomain)
                .toList();
    }

    private Optional<Patient> toDomain(Optional<PatientEntity> entity) {
        return entity.map(PatientPersistenceMapper::toDomain);
    }

}