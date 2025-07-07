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
    ResponseEntity<PacienteResponse> crearPaciente(PacienteRequest request);

    @Operation(summary = "Obtener todos los pacientes")
    ResponseEntity<List<PacienteResponse>> obtenerPacientes();

    @Operation(summary = "Buscar paciente por ID")
    ResponseEntity<PacienteResponse> obtenerPaciente(
            @Parameter(description = "ID único del paciente", example = "1")
            Long id
    );

    @Operation(summary = "Actualizar un paciente existente")
    ResponseEntity<PacienteResponse> actualizarPaciente(
            @Parameter(description = "ID único del paciente", example = "1")
            Long id,
            PacienteRequest request
    );

    @Operation(summary = "Eliminar un paciente por ID")
    ResponseEntity<Object> borrarPaciente(
            @Parameter(description = "ID único del paciente", example = "1")
            Long id
    );
}
