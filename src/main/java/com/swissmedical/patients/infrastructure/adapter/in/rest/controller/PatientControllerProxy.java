package com.swissmedical.patients.infrastructure.adapter.in.rest.controller;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.service.PatientService;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientCreateDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientResponseDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientCreateMapper;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientResponseMapper;
import com.swissmedical.patients.infrastructure.adapter.in.rest.mapper.PatientUpdateMapper;
import com.swissmedical.patients.shared.utils.DefaultValuesController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Gestión de Pacientes", description = "API para administrar pacientes del sistema")
public class PatientControllerProxy implements PatientApi {

  private final PatientController patientController;
  private final Logger logger = LoggerFactory.getLogger(PatientControllerProxy.class);

  public PatientControllerProxy(PatientController patientController) {
    this.patientController = patientController;
  }


  @Override
  @GetMapping()
  public ResponseEntity<List<PatientResponseDto>> getAll(
          @RequestParam(defaultValue = DefaultValuesController.NAME) String name,
          @RequestParam(defaultValue = DefaultValuesController.PAGE) int page,
          @RequestParam(defaultValue = DefaultValuesController.SIZE) int size
  ) {
    logger.info("Obteniendo pacientes con nombre: {}, página: {}, cantidad: {}", name, page, size);
    return patientController.getAll(name, page, size);
  }

  @Override
  @GetMapping("/dni/{dni}")
  public ResponseEntity<PatientResponseDto> getByDni(@PathVariable String dni) {
    logger.info("Obteniendo pacientes por DNI: {}", dni);
    return patientController.getByDni(dni);
  }

  @Override
  @GetMapping("/social-security/{socialSecurity}")
  public ResponseEntity<List<PatientResponseDto>> getBySocialSecurity(
          @PathVariable String socialSecurity,
          @RequestParam(defaultValue = DefaultValuesController.PAGE) int page,
          @RequestParam(defaultValue = DefaultValuesController.SIZE) int size
  ) {
    logger.info("Obteniendo pacientres por seguro social: {}, página: {}, cantidad: {}", socialSecurity, page, size);
    return patientController.getBySocialSecurity(socialSecurity, page, size);
  }

  @Override
  @PostMapping()
  public ResponseEntity<PatientResponseDto> create(@Valid @RequestBody PatientCreateDto patientCreateDto) {
    logger.info("Creando nuevo paciente: {}", patientCreateDto);
    return patientController.create(patientCreateDto);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<PatientResponseDto> update(@Valid @RequestBody PatientUpdateDto patientUpdateDto, @PathVariable Long id) {
    logger.info("Actualizando paciente con ID: {}, data: {}", id, patientUpdateDto);
    return patientController.update(patientUpdateDto, id);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    logger.info("Borrando paciente con ID: {}", id);
    return patientController.delete(id);
  }
}
