package com.tq.pacientes.services.servicesImpl;

import com.tq.pacientes.exceptions.DuplicatePatientException;
import com.tq.pacientes.exceptions.PatientDniNotFoundException;
import com.tq.pacientes.exceptions.PatientNotFoundException;
import com.tq.pacientes.exceptions.PatientAlreadyActiveException;
import com.tq.pacientes.models.Patient;
import com.tq.pacientes.repositories.PatientRepository;
import com.tq.pacientes.services.PatientService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient createPatient(Patient patient) {
        patientRepository.findByDni(patient.getDni())
                .ifPresent(p -> { throw new DuplicatePatientException(patient.getDni()); });

        patient.setCreationDate(LocalDateTime.now());
        patient.setLastModifiedDate(LocalDateTime.now());
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> listAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findByDni(String dni) {
        return patientRepository.findByDni(dni)
                .or(() -> { throw new PatientDniNotFoundException(dni); });
    }

    @Override
    public List<Patient> searchByFirstName(String name) {
        return patientRepository.findByFirstNameContainingIgnoreCase(name);
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails) {
        return patientRepository.findById(id)
                .map(patient -> {
                    if (patientDetails.getDni() != null && !patient.getDni().equals(patientDetails.getDni())) {
                        patientRepository.findByDni(patientDetails.getDni())
                                .ifPresent(p -> { throw new DuplicatePatientException(patientDetails.getDni()); });
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
                    if (patientDetails.getEmail() != null) {
                        patient.setEmail(patientDetails.getEmail());
                    }
                    if (patientDetails.getPhoneNumber() != null) {
                        patient.setPhoneNumber(patientDetails.getPhoneNumber());
                    }

                    patient.setLastModifiedDate(LocalDateTime.now());
                    return patientRepository.save(patient);
                })
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        patient.setActive(false);
        patient.setLastModifiedDate(LocalDateTime.now());
        patientRepository.save(patient);
    }

    @Override
    public void activatePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        if (patient.getActive()) {
            throw new PatientAlreadyActiveException(id);
        }

        patient.setActive(true);
        patient.setLastModifiedDate(LocalDateTime.now());
        patientRepository.save(patient);
    }
}
