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

    public Patient getPatientByDni(String dni) {
        return patientRepository.findByDniSP(dni)
                .orElseThrow(() -> new PatientNotFoundException("Patient with DNI " + dni + " does not exist."));
    }

    public List<Patient> getPatientsBySocialSecurity(String socialSecurity, int limit, int offset) {
        List<Patient> patients = patientRepository.findBySocialSecuritySP(socialSecurity, limit, offset);

        if (patients.isEmpty()) {
            throw new PatientNotFoundException("No patients found with social security " + socialSecurity + ".");
        }

        return patients;
    }

    public List<Patient> getPatientByFirstName(String firstName) {
        List<Patient> patients = patientRepository.findByFirstNameOrLastNameSP(firstName);

        return patients;
    }

    public List<Patient> getPatientByFirstNameOrLastName(String firstName, String lastName) {
        List<Patient> patients = patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName, lastName);

        return patients;
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

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient with ID " + id + " does not exist.");
        }
        patientRepository.deleteById(id);
    }

}
