package com.teamcubation.api.pacientes.controller;

import com.teamcubation.api.pacientes.dto.PacienteRequest;
import com.teamcubation.api.pacientes.dto.PacienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Pacientes", description = "Operaciones relacionadas con pacientes")
public interface IPacienteAPI {

    @Operation(summary = "Crear un nuevo paciente")
    ResponseEntity<PacienteResponse> crear(PacienteRequest request);

    @Operation(summary = "Obtener todos los pacientes")
    ResponseEntity<List<PacienteResponse>> obtenerTodos(
            @Parameter(description = "DNI completo del paciente", example = "12345678")
            String dni,
            @Parameter(description = "Nombre completo o parcial del paciente", example = "Roberto")
            String nombre);

    @Operation(summary = "Buscar paciente por ID")
    ResponseEntity<PacienteResponse> obtenerPorID(
            @Parameter(description = "ID único del paciente", example = "1")
            long id
    );

    @Operation(summary = "Actualizar un paciente existente")
    ResponseEntity<PacienteResponse> actualizarPorID(
            @Parameter(description = "ID único del paciente", example = "1")
            long id,
            PacienteRequest request
    );

    @Operation(summary = "Eliminar un paciente por ID")
    ResponseEntity<Void> borrarPorID(
            @Parameter(description = "ID único del paciente", example = "1")
            long id
    );
}
