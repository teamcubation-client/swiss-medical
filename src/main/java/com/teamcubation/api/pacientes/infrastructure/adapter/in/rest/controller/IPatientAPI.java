package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.controller;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Pacientes", description = "Operaciones relacionadas con pacientes")
public interface IPatientAPI {

    @Operation(summary = "Crear un nuevo paciente")
    ResponseEntity<ApiResponse<PatientResponse>> create(PatientRequest request);

    @Operation(summary = "Obtener todos los pacientes")
    ResponseEntity<ApiResponse<List<PatientResponse>>> getAll(
            @Parameter(description = "DNI completo del paciente", example = "12345678")
            String dni,
            @Parameter(description = "Nombre completo o parcial del paciente", example = "Roberto")
            String name);

    @Operation(summary = "Buscar paciente por ID")
    ResponseEntity<ApiResponse<PatientResponse>> getById(
            @Parameter(description = "ID único del paciente", example = "1")
            long id
    );

    @Operation(summary = "Actualizar un paciente existente")
    ResponseEntity<ApiResponse<PatientResponse>> updateById(
            @Parameter(description = "ID único del paciente", example = "1")
            long id,
            PatientRequest request
    );

    @Operation(summary = "Eliminar un paciente por ID")
    ResponseEntity<Void> deleteById(
            @Parameter(description = "ID único del paciente", example = "1")
            long id
    );

    @Operation(summary = "Buscar paciente por DNI")
    ResponseEntity<ApiResponse<PatientResponse>> getByDni(
            @Parameter(description = "DNI completo del paciente", example = "12345678")
            String dni
    );

    @Operation(summary = "Buscar paciente por nombre")
    ResponseEntity<ApiResponse<List<PatientResponse>>> getByName(
            @Parameter(description = "Nombre parcial o completo del paciente", example = "Ana")
            String name
    );

    @Operation(summary = "Buscar paciente por obra social")
    ResponseEntity<ApiResponse<List<PatientResponse>>> getByHealthInsuranceProvider(
            @Parameter(description = "Nombre parcial o completo de obra social", example = "Swiss Medical")
            String healthInsuranceProvider,
            @Parameter(description = "Número de página (0 basado)", example = "0")
            int page,
            @Parameter(description = "Cantidad de elementos por página", example = "10")
            int size
    );

    @Operation(summary = "Permite exportar lista de pacientes en el formato seleccionado")
    ResponseEntity<ApiResponse<String>> exportPatients(@Parameter(description = "Formato elegido", example = "csv") String format);
}
