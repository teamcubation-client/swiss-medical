package com.tq.pacientes.infrastructure.adapter.in.rest;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.in.PatientUseCase;
import com.tq.pacientes.infrastructure.adapter.in.rest.documentation.PatientControllerAPI;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.tq.pacientes.infrastructure.adapter.in.rest.mapper.PatientRestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientController implements PatientControllerAPI {

    private final PatientUseCase patientUseCase;
    private final PatientRestMapper patientRestMapper;

    public PatientController(PatientUseCase patientUseCase, PatientRestMapper patientRestMapper) {
        this.patientUseCase = patientUseCase;
        this.patientRestMapper = patientRestMapper;
    }

    @PostMapping
    public ResponseEntity<PatientResponse> create(@RequestBody PatientRequest patientRequest) {
        Patient patient = patientRestMapper.toDomain(patientRequest);
        Patient created = patientUseCase.create(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientRestMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> search(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String firstName
    ) {
        List<Patient> results;
        if (dni != null && !dni.isEmpty()) {
            Optional<Patient> patient = patientUseCase.getByDni(dni);
            results = patient.map(List::of).orElse(List.of());
        } else if (firstName != null && !firstName.isEmpty()) {
results = patientUseCase.searchByFirstName(firstName);
        } else {
            results = patientUseCase.getAll();
        }
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<PatientResponse> dtos = results.stream()
                .map(patientRestMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/first-name/{firstName}")
    public ResponseEntity<List<PatientResponse>> searchByFirstName(@PathVariable String firstName) {
        List<Patient> results = patientUseCase.searchByFirstName(firstName);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<PatientResponse> dtos = results.stream()
                .map(patientRestMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PatientResponse> findByDni(@PathVariable String dni) {
        Optional<Patient> patient = patientUseCase.getByDni(dni);
        return patient.map(value -> ResponseEntity.ok(patientRestMapper.toResponse(value)))
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> findById(@PathVariable Long id) {
        Optional<Patient> patient = patientUseCase.getById(id);
        return patient.map(value -> ResponseEntity.ok(patientRestMapper.toResponse(value)))
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/health-insurance")
    public ResponseEntity<List<PatientResponse>> getByHealthInsurance(
            @RequestParam String healthInsurance,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int offset = page * size;
        List<Patient> patients = patientUseCase.searchByHealthInsurancePaginated(healthInsurance, size, offset);
        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<PatientResponse> dtos = patients.stream()
                .map(patientRestMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatientResponse> update(
            @PathVariable Long id,
            @RequestBody PatientRequest request
    ) {
        Patient patientUpdates = patientRestMapper.toDomain(request);
        Patient updated = patientUseCase.update(id, patientUpdates);
        return ResponseEntity.ok(patientRestMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        patientUseCase.activate(id);
        return ResponseEntity.ok().build();
    }
} 