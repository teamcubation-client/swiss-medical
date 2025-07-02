package com.swissmedical.patients.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swissmedical.patients.dto.PatientDto;
import com.swissmedical.patients.entity.Patient;
import com.swissmedical.patients.exceptions.PatientNotFoundException;
import com.swissmedical.patients.mappers.PatientMapper;
import com.swissmedical.patients.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping()
    public ResponseEntity<List<Patient>> getMethodName(@RequestParam(defaultValue = "") String name) {
        if (name.isEmpty()) {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        }

        List<Patient> patients = patientService.getPatientByFirstNameOrLastName(name, name);

        if (patients.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Patient> getPatientByDni(@PathVariable String dni) {
        try {
            Patient patient = patientService.getPatientByDni(dni);
            return ResponseEntity.ok(patient);
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createPatient(@Valid @RequestBody PatientDto patientDto) {
        Patient patient = PatientMapper.toEntity(patientDto);

        if (patient == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Patient createdPatient = patientService.createPatient(patient);

            return ResponseEntity.status(201).body(createdPatient);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating patient: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@RequestBody PatientDto patientDto, @PathVariable Long id) {
        Patient patient = PatientMapper.toEntity(patientDto);

        if (patient == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Patient updatedPatient = patientService.updatePatient(id, patient);
            return ResponseEntity.ok(updatedPatient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
