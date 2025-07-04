package com.swissmedical.patients.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.swissmedical.patients.dto.PatientDto;
import com.swissmedical.patients.entity.Patient;
import com.swissmedical.patients.exceptions.PatientNotFoundException;
import com.swissmedical.patients.mappers.PatientMapper;
import com.swissmedical.patients.service.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping()
    @Operation(summary = "Obtener una lista con todos los pacientes o buscar por nombre")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron pacientes con el nombre especificado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener la lista de pacientes")
    })
    public ResponseEntity<List<Patient>> getMethodName(@RequestParam(defaultValue = "") String name) {
        if (name.isEmpty()) {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        }

        List<Patient> patients = patientService.getPatientByFirstNameOrLastName(name, name);

        if (patients.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{dni}")
    @Operation(summary = "Obtener un paciente por su DNI")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado con el DNI especificado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el paciente")
    })
    public ResponseEntity<Patient> getPatientByDni(@PathVariable String dni) {
        try {
            Patient patient = patientService.getPatientByDni(dni);
            return ResponseEntity.ok(patient);
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping()
    @Operation(summary = "Crear un nuevo paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Paciente creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos del paciente incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al crear el paciente")
    })
    public ResponseEntity<?> createPatient(@Valid @RequestBody PatientDto patientDto) {
        Patient patient = PatientMapper.toEntity(patientDto);

        if (patient == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Patient createdPatient = patientService.createPatient(patient);

            return ResponseEntity.status(201).body(createdPatient);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating patient: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un paciente existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos del paciente incorrectos"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado con el ID especificado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar el paciente")
    })
    public ResponseEntity<Patient> updatePatient(@RequestBody PatientDto patientDto, @PathVariable Long id) {
        Patient patient = PatientMapper.toEntity(patientDto);

        if (patient == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Patient updatedPatient = patientService.updatePatient(id, patient);
            return ResponseEntity.ok(updatedPatient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un paciente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Paciente eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado con el ID especificado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al eliminar el paciente")
    })
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
