package com.swissmedical.patients.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swissmedical.patients.entity.Patient;
import com.swissmedical.patients.exceptions.PatientDuplicateException;
import com.swissmedical.patients.exceptions.PatientNotFoundException;
import com.swissmedical.patients.repository.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient with ID " + id + " does not exist.")
        );
    }

    public Patient getPatientByDni(String dni) {
        return patientRepository.findByDni(dni)
                .orElseThrow(() -> new PatientNotFoundException("Patient with DNI " + dni + " does not exist."));
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email).orElseThrow(
                () -> new PatientNotFoundException("Patient with email " + email + " does not exist.")
        );
    }

    public List<Patient> getPatientByFirstNameOrLastName(String firstName, String lastName) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName, lastName);
    }

    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByDni(patient.getDni())) {
            throw new PatientDuplicateException("Patient with DNI " + patient.getDni() + " already exists.");
        }

        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new PatientDuplicateException("Patient with email " + patient.getEmail() + " already exists.");
        }

        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient with ID " + id + " does not exist.");
        }

        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient with ID " + id + " does not exist."));

        existingPatient.setFirstName(patientDetails.getFirstName());
        existingPatient.setLastName(patientDetails.getLastName());
        existingPatient.setEmail(patientDetails.getEmail());
        existingPatient.setPhoneNumber(patientDetails.getPhoneNumber());
        existingPatient.setDni(patientDetails.getDni());
        existingPatient.setSocialSecurity(patientDetails.getSocialSecurity());

        return patientRepository.save(existingPatient);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient with ID " + id + " does not exist.");
        }
        patientRepository.deleteById(id);
    }

}
