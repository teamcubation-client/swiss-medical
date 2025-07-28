package com.tq.pacientes.infrastructure.adapter.in.rest;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.service.PatientService;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.tq.pacientes.infrastructure.adapter.in.rest.mapper.PatientRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Ejemplo de MALA PRÁCTICA: Este controlador viola el Principio de Inversión de Dependencias (DIP)
 * al depender directamente de la implementación concreta (PatientService) en lugar de la abstracción (PatientUseCase).
 * Problemas que esto causa:
 * 1. Acoplamiento fuerte con la implementación concreta
 * 2. Dificulta el testing (más difícil hacer mocks)
 * 3. Hace más complicado cambiar la implementación en el futuro
 * 4. Viola el principio de sustitución de Liskov
 */
@RestController
@RequestMapping("/api/bad-practices/patients")
@RequiredArgsConstructor
public class BadPatientController {

    // MALA PRÁCTICA: Depender directamente de la implementación concreta en lugar de la interfaz
    private final PatientService patientService;
    private final PatientRestMapper patientRestMapper;

    @PostMapping
    public ResponseEntity<PatientResponse> create(@RequestBody PatientRequest patientRequest) {
        Patient patient = patientRestMapper.toDomain(patientRequest);
        Patient created = patientService.create(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientRestMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAll() {
        List<Patient> patients = patientService.getAll();
        if (patients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<PatientResponse> dtos = patients.stream()
                .map(patientRestMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> findById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getById(id);
        return patient.map(value -> ResponseEntity.ok(patientRestMapper.toResponse(value)))
                .orElse(ResponseEntity.noContent().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatientResponse> update(
            @PathVariable Long id,
            @RequestBody PatientRequest request
    ) {
        Patient patientUpdates = patientRestMapper.toDomain(request);
        Patient updated = patientService.update(id, patientUpdates);
        return ResponseEntity.ok(patientRestMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        patientService.activate(id);
        return ResponseEntity.ok().build();
    }
}
