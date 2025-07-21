package com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.repository;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.application.domain.port.out.PatientPortOut;
import com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.entity.PatientEntity;
import com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.mapper.PatientPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PatientRepository implements PatientPortOut {
    private final IPatientRepository iPatientRepository;

    public PatientRepository(IPatientRepository iPatientRepository) {
        this.iPatientRepository = iPatientRepository;
    }

    @Override
    public Patient save(Patient patient) {
        PatientEntity patientEntity = PatientPersistenceMapper.toEntity(patient);
        PatientEntity saved = this.iPatientRepository.save(patientEntity);
        return PatientPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<Patient> findAll(String dni, String name) {
        List<PatientEntity> patientsFound = this.iPatientRepository.search(dni, name);
        List<Patient> patients = new ArrayList<>();
        for (PatientEntity patient : patientsFound) {
            patients.add(PatientPersistenceMapper.toDomain(patient));
        }
        return patients;
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return this.iPatientRepository.findById(id)
                .map(PatientPersistenceMapper::toDomain);
    }

    @Override
    public Patient updateById(Patient patient) {
        PatientEntity entity = PatientPersistenceMapper.toEntity(patient);
        PatientEntity updated = this.iPatientRepository.save(entity);
        return PatientPersistenceMapper.toDomain(updated);
    }

    @Override
    public void deleteById(long id) {
        this.iPatientRepository.deleteById(id);
    }

    @Override
    public Optional<Patient> findByDni(String dni) {
        return this.iPatientRepository.findByDni(dni)
                .map(PatientPersistenceMapper::toDomain);
    }

    @Override
    public List<Patient> findByName(String name) {
        List<PatientEntity> patientEntities = this.iPatientRepository.findByName(name);
        List<Patient> patients = new ArrayList<>();
        for (PatientEntity patient : patientEntities) {
            patients.add(PatientPersistenceMapper.toDomain(patient));
        }
        return patients;
    }

    @Override
    public List<Patient> findByHealthInsuranceProvider(String healthInsuranceProvider, int limit, int offset) {
        List<PatientEntity> patientEntities = this.iPatientRepository.findByHealthInsuranceProvider(healthInsuranceProvider, limit, offset);
        List<Patient> patients = new ArrayList<>();
        for (PatientEntity patient : patientEntities) {
            patients.add(PatientPersistenceMapper.toDomain(patient));
        }
        return patients;
    }
}
