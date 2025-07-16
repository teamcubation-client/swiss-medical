package com.swissmedical.patients.controller;

import java.util.List;

import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;
import org.springframework.http.HttpStatus;
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

import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientCreateDto;
import com.swissmedical.patients.entity.Patient;
import com.swissmedical.patients.mappers.PatientMapper;
import com.swissmedical.patients.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientController implements PatientApi {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<Patient>> getAll(@RequestParam(defaultValue = "") String name,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size) {

       return ResponseEntity.ok(patientService.getAllPatients(name , page, size));
    }

    @Override
    @GetMapping("/dni/{dni}")
    public ResponseEntity<Patient> getByDni(@PathVariable String dni) {
        return ResponseEntity.ok(patientService.getPatientByDni(dni));
    }

    @Override
    @GetMapping("/social-security/{socialSecurity}")
    public ResponseEntity<List<Patient>> getBySocialSecurity(@PathVariable String socialSecurity,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size
    ) {
        if (page <= 0 || size < 0) {
            throw new IllegalArgumentException("Limit must be greater than 0 and offset must be non-negative.");
        }

        List<Patient> patients = patientService.getPatientsBySocialSecurity(socialSecurity, page, size);

        return ResponseEntity.ok(patients);
    }

    @Override
    @PostMapping()
    public ResponseEntity<Patient> create(@Valid @RequestBody PatientCreateDto patientCreateDto) {
        Patient patient = PatientMapper.toEntity(patientCreateDto);

        Patient createdPatient = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(@Valid @RequestBody PatientUpdateDto patientUpdateDto, @PathVariable Long id) {
        Patient patient = PatientMapper.toEntity(patientUpdateDto);

        Patient updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);

    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();

    }

}
