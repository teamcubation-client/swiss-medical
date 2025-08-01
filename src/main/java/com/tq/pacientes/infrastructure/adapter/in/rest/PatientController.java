package com.tq.pacientes.infrastructure.adapter.in.rest;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.in.PatientUseCase;
import com.tq.pacientes.infrastructure.adapter.in.rest.documentation.PatientControllerAPI;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.tq.pacientes.infrastructure.adapter.in.rest.mapper.PatientRestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

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
            Patient patient = patientUseCase.getByDni(dni);
            results = List.of(patient);
        } else if (firstName != null && !firstName.isEmpty()) {
            results = patientUseCase.searchByFirstName(firstName);
        } else {
            results = patientUseCase.getAll();
        }
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(toResponseList(results));
    }

    @GetMapping("/first-name/{firstName}")
    public ResponseEntity<List<PatientResponse>> searchByFirstName(@PathVariable String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<Patient> results = patientUseCase.searchByFirstName(firstName);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(toResponseList(results));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PatientResponse> findByDni(@PathVariable String dni) {
        if (dni == null || dni.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Patient patient = patientUseCase.getByDni(dni);
        return ResponseEntity.ok(patientRestMapper.toResponse(patient));
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
        return ResponseEntity.ok(toResponseList(patients));
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
    public ResponseEntity<PatientResponse> activate(@PathVariable Long id) {
        Patient activatedPatient = patientUseCase.activate(id);
        return ResponseEntity.ok(patientRestMapper.toResponse(activatedPatient));
    }

    private List<PatientResponse> toResponseList(List<Patient> patients) {
        return patients.stream()
                .map(patientRestMapper::toResponse)
                .toList();
    }
} 