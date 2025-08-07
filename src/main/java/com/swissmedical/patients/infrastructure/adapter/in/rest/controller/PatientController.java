package com.swissmedical.patients.infrastructure.adapter.in.rest.controller;

import java.util.List;

import com.swissmedical.patients.application.domain.command.CreatePatientCommand;
import com.swissmedical.patients.application.domain.command.UpdatePatientCommand;
import com.swissmedical.patients.application.service.PatientService;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientResponseDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientCreateMapper;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientResponseMapper;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientUpdateMapper;
import com.swissmedical.patients.infrastructure.adapter.in.rest.response.BaseResponse;
import com.swissmedical.patients.infrastructure.adapter.in.rest.response.SuccessResponse;
import com.swissmedical.patients.shared.utils.DefaultValuesController;
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
  public ResponseEntity<BaseResponse<List<PatientResponseDto>>> getAll(
          @RequestParam(defaultValue = DefaultValuesController.NAME) String name,
          @RequestParam(defaultValue = DefaultValuesController.PAGE) int page,
          @RequestParam(defaultValue = DefaultValuesController.SIZE) int size
  ) {
    return ResponseEntity.ok().body(new SuccessResponse<>(patientService.getAll(name, page, size)
            .stream()
            .map(PatientResponseMapper::toDto)
            .toList()));
  }

  @Override
  @GetMapping("/dni/{dni}")
  public ResponseEntity<BaseResponse<PatientResponseDto>> getByDni(@PathVariable String dni) {
    return ResponseEntity.status(HttpStatus.OK).body(
            new SuccessResponse<>(PatientResponseMapper.toDto(patientService.getByDni(dni)))
    );
  }


  @Override
  @GetMapping("/social-security/{socialSecurity}")
  public ResponseEntity<BaseResponse<List<PatientResponseDto>>> getBySocialSecurity(
          @PathVariable String socialSecurity,
          @RequestParam(defaultValue = DefaultValuesController.PAGE) int page,
          @RequestParam(defaultValue = DefaultValuesController.SIZE) int size
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(patientService.getBySocialSecurity(socialSecurity, page, size)
            .stream()
            .map(PatientResponseMapper::toDto)
            .toList()));
  }


  @Override
  @PostMapping()
  public ResponseEntity<BaseResponse<PatientResponseDto>> create(@Valid @RequestBody PatientCreateDto patientCreateDto) {
    CreatePatientCommand patient = PatientCreateMapper.toCommand(patientCreateDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(
            new SuccessResponse<>(PatientResponseMapper.toDto(patientService.create(patient)))
    );
  }


  @Override
  @PutMapping("/{id}")
  public ResponseEntity<BaseResponse<PatientResponseDto>> update(@Valid @RequestBody PatientUpdateDto patientUpdateDto, @PathVariable Long id) {
    UpdatePatientCommand updatePatientCommand = PatientUpdateMapper.toCommand(patientUpdateDto);
    return ResponseEntity.ok().body(
            new SuccessResponse<>(PatientResponseMapper.toDto(patientService.update(id, updatePatientCommand)))
    );
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    patientService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
