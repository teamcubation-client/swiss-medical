package com.tq.pacientes.application.service;

import com.tq.pacientes.shared.exceptions.DuplicatePatientException;
import com.tq.pacientes.shared.exceptions.PatientAlreadyActiveException;
import com.tq.pacientes.shared.exceptions.PatientDniNotFoundException;
import com.tq.pacientes.shared.exceptions.PatientNotFoundException;
import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.in.PatientUseCase;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService implements PatientUseCase {

    private final PatientRepositoryPort patientRepositoryPort;

    public PatientService(PatientRepositoryPort patientRepositoryPort) {
        this.patientRepositoryPort = patientRepositoryPort;
    }

    @Override
    public Patient create(Patient patient) {
        if (patientRepositoryPort.findByDni(patient.getDni()).isPresent()) {
            throw new DuplicatePatientException(patient.getDni());
        }
        patient.setCreationDate(LocalDateTime.now());
        patient.setLastModifiedDate(LocalDateTime.now());
        return patientRepositoryPort.save(patient);
    }

    @Override
    public List<Patient> getAll() {
        return patientRepositoryPort.findAll();
    }

    @Override
    public Optional<Patient> getById(Long id) {
        return patientRepositoryPort.findById(id);
    }

    @Override
    public Optional<Patient> getByDni(String dni) {
        Optional<Patient> patientOpt = patientRepositoryPort.findByDni(dni);
        if (patientOpt.isEmpty()) {
            throw new PatientDniNotFoundException(dni);
        }
        return patientOpt;
    }

    @Override
    public List<Patient> searchByFirstName(String firstName) {
        return patientRepositoryPort.searchByFirstName(firstName);
    }

    @Override
    public List<Patient> searchByHealthInsurancePaginated(String healthInsurance, int limit, int offset) {
        return patientRepositoryPort.searchByHealthInsurancePaginated(healthInsurance, limit, offset);
    }

    @Override
    public Patient update(Long id, Patient patientDetails) {
        Patient patient = getById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        if (patientDetails.getDni() != null && !patient.getDni().equals(patientDetails.getDni())) {
            if (patientRepositoryPort.findByDni(patientDetails.getDni()).isPresent()) {
                throw new DuplicatePatientException(patientDetails.getDni());
            }
            patient.setDni(patientDetails.getDni());
        }
        if (patientDetails.getFirstName() != null) {
            patient.setFirstName(patientDetails.getFirstName());
        }
        if (patientDetails.getLastName() != null) {
            patient.setLastName(patientDetails.getLastName());
        }
        if (patientDetails.getHealthInsurance() != null) {
            patient.setHealthInsurance(patientDetails.getHealthInsurance());
        }
        if (patientDetails.getHealthPlan() != null) {
            patient.setHealthPlan(patientDetails.getHealthPlan());
        }
        if (patientDetails.getAddress() != null) {
            patient.setAddress(patientDetails.getAddress());
        }
        if (patientDetails.getEmail() != null) {
            patient.setEmail(patientDetails.getEmail());
        }
        if (patientDetails.getPhoneNumber() != null) {
            patient.setPhoneNumber(patientDetails.getPhoneNumber());
        }
        patient.setLastModifiedDate(LocalDateTime.now());
        return patientRepositoryPort.update(patient);
    }

    @Override
    public void delete(Long id) {
        Patient patient = getById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        patient.setActive(false);
        patient.setLastModifiedDate(LocalDateTime.now());
        patientRepositoryPort.update(patient);
    }

    @Override
    public void activate(Long id) {
        Patient patient = getById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        if (Boolean.TRUE.equals(patient.getActive())) {
            throw new PatientAlreadyActiveException(id);
        }
        patient.setActive(true);
        patient.setLastModifiedDate(LocalDateTime.now());
        patientRepositoryPort.update(patient);
    }
} 