package com.swissmedical.patients.infrastructure.adapter.in.rest.controller;

import java.util.List;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.service.PatientService;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientResponseDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientCreateMapper;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientResponseMapper;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientUpdateMapper;
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
    public List<PatientResponseDto> getAll(@RequestParam(defaultValue = "") String name,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size) {

       return patientService.getAll(name, page, size)
                .stream()
                .map(PatientResponseMapper::toDto)
                .toList();
    }

    @Override
    @GetMapping("/dni/{dni}")
    public PatientResponseDto getByDni(@PathVariable String dni) {
        Patient patient = patientService.getByDni(dni);
        return PatientResponseMapper.toDto(patient);
    }

    @Override
    @GetMapping("/social-security/{socialSecurity}")
    public List<PatientResponseDto> getBySocialSecurity(@PathVariable String socialSecurity,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size
    ) {
        return patientService.getBySocialSecurity(socialSecurity, page, size)
                .stream()
                .map(PatientResponseMapper::toDto)
                .toList();
    }

    @Override
    @PostMapping()
    public PatientResponseDto create(@Valid @RequestBody PatientCreateDto patientCreateDto) {
        Patient patient = PatientCreateMapper.toDomain(patientCreateDto);
        return PatientResponseMapper.toDto(patientService.create(patient));
    }

    @Override
    @PutMapping("/{id}")
    public PatientResponseDto update(@Valid @RequestBody PatientUpdateDto patientUpdateDto, @PathVariable Long id) {
        Patient patientDetails = PatientUpdateMapper.toDomain(patientUpdateDto);
        return PatientResponseMapper.toDto(patientService.update(id, patientDetails));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
