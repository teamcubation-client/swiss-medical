package com.swissmedical.patients.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.swissmedical.patients.dto.PatientDto;
import com.swissmedical.patients.entity.Patient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface PatientApi {

    @Operation(summary = "Obtener una lista con todos los pacientes o buscar por nombre")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron pacientes con el nombre especificado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener la lista de pacientes")
    })
    public ResponseEntity<List<Patient>> getPatients(String name);

    @Operation(summary = "Obtener un paciente por su DNI")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado con el DNI especificado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el paciente")
    })
    public ResponseEntity<Patient> getPatientByDni(String dni);

    @Operation(summary = "Crear un nuevo paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Paciente creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos del paciente incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al crear el paciente")
    })
    public ResponseEntity<?> createPatient(PatientDto patientDto);

    @Operation(summary = "Actualizar un paciente existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos del paciente incorrectos"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado con el ID especificado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar el paciente")
    })
    public ResponseEntity<Patient> updatePatient(PatientDto patientDto, Long id);

    @Operation(summary = "Eliminar un paciente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Paciente eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado con el ID especificado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al eliminar el paciente")
    })
    public ResponseEntity<Void> deletePatient(Long id);
}
