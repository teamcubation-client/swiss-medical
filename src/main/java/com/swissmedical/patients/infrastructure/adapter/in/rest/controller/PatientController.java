package com.swissmedical.patients.infrastructure.adapter.in.rest.controller;

import java.util.List;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.service.PatientService;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientResponseDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientCreateMapper;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientResponseMapper;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientUpdateMapper;
import com.swissmedical.patients.shared.utils.DefaultValuesController;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Gestión de Pacientes", description = "API para administrar pacientes del sistema")
public class PatientController implements PatientApi {

  private final PatientService patientService;

  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }


  @Override
  @GetMapping()
  public ResponseEntity<List<PatientResponseDto>> getAll(
          @RequestParam(defaultValue = DefaultValuesController.NAME) String name,
          @RequestParam(defaultValue = DefaultValuesController.PAGE) int page,
          @RequestParam(defaultValue = DefaultValuesController.SIZE) int size
  ) {
    return ResponseEntity.ok(patientService.getAll(name, page, size)
            .stream()
            .map(PatientResponseMapper::toDto)
            .toList());
  }

  @Override
  @GetMapping("/dni/{dni}")
  public ResponseEntity<PatientResponseDto> getByDni(@PathVariable String dni) {
    return ResponseEntity.ok(PatientResponseMapper.toDto(patientService.getByDni(dni)));
  }


  @Override
  @GetMapping("/social-security/{socialSecurity}")
  public ResponseEntity<List<PatientResponseDto>> getBySocialSecurity(
          @PathVariable String socialSecurity,
          @RequestParam(defaultValue = DefaultValuesController.PAGE) int page,
          @RequestParam(defaultValue = DefaultValuesController.SIZE) int size
  ) {
    return ResponseEntity.ok(patientService.getBySocialSecurity(socialSecurity, page, size)
            .stream()
            .map(PatientResponseMapper::toDto)
            .toList());
  }


  @Override
  @PostMapping()
  public ResponseEntity<PatientResponseDto> create(@Valid @RequestBody PatientCreateDto patientCreateDto) {
    Patient patient = PatientCreateMapper.toDomain(patientCreateDto);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(PatientResponseMapper.toDto(patientService.create(patient)));
  }


  @Override
  @PutMapping("/{id}")
  public ResponseEntity<PatientResponseDto> update(@Valid @RequestBody PatientUpdateDto patientUpdateDto, @PathVariable Long id) {
    Patient patientDetails = PatientUpdateMapper.toDomain(patientUpdateDto);
    return ResponseEntity.ok(PatientResponseMapper.toDto(patientService.update(id, patientDetails)));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    patientService.delete(id);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
