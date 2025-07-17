package com.tq.pacientes.controllers;

import com.tq.pacientes.documentation.IPatientController;
import com.tq.pacientes.dtos.PatientDTO;
import com.tq.pacientes.mappers.PatientMapper;
import com.tq.pacientes.models.Patient;
import com.tq.pacientes.services.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientController implements IPatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    public PatientController(PatientService patientService, PatientMapper patientMapper) {
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
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String firstName
    ) {
        List<Patient> results;

        if (dni != null && firstName != null) {
            results = new ArrayList<>();
            patientService.findByDni(dni).ifPresent(patient -> {
                if (patient.getFirstName().equalsIgnoreCase(firstName)) {
                    results.add(patient);
                }
            });
        } else if (dni != null) {
            results = new ArrayList<>();
            patientService.findByDni(dni).ifPresent(results::add);
        } else if (firstName != null) {
            results = patientService.searchByFirstName(firstName);
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

    @GetMapping("/first-name/{firstName}")
    public ResponseEntity<List<PatientDTO>> searchByFirstName(@PathVariable String firstName) {
        List<Patient> results = patientService.searchByFirstName(firstName);
        List<PatientDTO> dtos = results.stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
        return dtos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(dtos);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PatientDTO> findByDni(@PathVariable String dni) {
        return patientService.findByDni(dni)
                .map(patient -> ResponseEntity.ok(patientMapper.toDto(patient)))
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable Long id) {
        return patientService.findById(id)
                .map(patient -> ResponseEntity.ok(patientMapper.toDto(patient)))
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/health-insurance")
    public ResponseEntity<List<PatientDTO>> getByHealthInsurance(
            @RequestParam String healthInsurance,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        int offset = page * size;

        var patients = patientService.searchByHealthInsurancePaginated(healthInsurance, size, offset);

        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PatientDTO> dtos = patients.stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatientDTO> update(
            @PathVariable Long id,
            @RequestBody PatientDTO dto
    ) {
        Patient patientUpdates = new Patient();
        if (dto.dni() != null) patientUpdates.setDni(dto.dni());
        if (dto.firstName() != null) patientUpdates.setFirstName(dto.firstName());
        if (dto.lastName() != null) patientUpdates.setLastName(dto.lastName());
        if (dto.healthInsurance() != null) patientUpdates.setHealthInsurance(dto.healthInsurance());
        if (dto.healthPlan() != null) patientUpdates.setHealthPlan(dto.healthPlan());
        if (dto.address() != null) patientUpdates.setAddress(dto.address());
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
