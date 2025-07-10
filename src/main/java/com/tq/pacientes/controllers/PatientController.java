package com.tq.pacientes.controllers;

import com.tq.pacientes.documentation.IPacienteController;
import com.tq.pacientes.dtos.PatientDTO;
import com.tq.pacientes.mappers.PatientMapper;
import com.tq.pacientes.models.Patient;
import com.tq.pacientes.services.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PacienteController implements IPacienteController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    public PacienteController(PatientService patientService, PatientMapper patientMapper) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    @PostMapping
    public ResponseEntity<PatientDTO> create(@RequestBody PatientDTO patientDTO) {
        Patient patient = patientMapper.toEntity(patientDTO);
        Patient createdPatient = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientMapper.toDto(createdPatient));
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> search(
            @RequestParam(required = false) String idNumber,
            @RequestParam(required = false) String name
    ) {
        List<Patient> results;

        if (idNumber != null) {
            return patientService.findByDni(idNumber)
                    .map(p -> ResponseEntity.ok(List.of(patientMapper.toDto(p))))
                    .orElse(ResponseEntity.noContent().build());
        } else if (name != null) {
            results = patientService.searchByFirstName(name);
        } else {
            results = patientService.listAllPatients();
        }

        List<PatientDTO> dtos = results.stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());

        return dtos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable Long id) {
        return patientService.findById(id)
                .map(patient -> ResponseEntity.ok(patientMapper.toDto(patient)))
                .orElse(ResponseEntity.noContent().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatientDTO> update(
            @PathVariable Long id,
            @RequestBody PatientDTO dto
    ) {
        Patient patientUpdates = new Patient();
        if (dto.idNumber() != null) patientUpdates.setDni(dto.idNumber());
        if (dto.firstName() != null) patientUpdates.setFirstName(dto.firstName());
        if (dto.lastName() != null) patientUpdates.setLastName(dto.lastName());
        if (dto.healthInsurance() != null) patientUpdates.setHealthInsurance(dto.healthInsurance());
        if (dto.email() != null) patientUpdates.setEmail(dto.email());
        if (dto.phoneNumber() != null) patientUpdates.setPhoneNumber(dto.phoneNumber());

        Patient updatedPatient = patientService.updatePatient(id, patientUpdates);
        return ResponseEntity.ok(patientMapper.toDto(updatedPatient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        patientService.activatePatient(id);
        return ResponseEntity.ok().build();
    }
}
