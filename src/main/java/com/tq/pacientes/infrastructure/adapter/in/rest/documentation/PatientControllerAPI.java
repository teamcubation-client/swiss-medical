package com.tq.pacientes.infrastructure.adapter.in.rest.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;

import java.util.List;

@Tag(name = "Pacientes", description = "API de Pacientes")
public interface PatientControllerAPI {

    @Operation(summary = "Crear un paciente", description = "Registra un nuevo paciente en el sistema. El DNI debe ser único. Si el DNI ya existe, se devuelve un error 409.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado"),
            @ApiResponse(responseCode = "409", description = "Paciente duplicado"),
    })
    ResponseEntity<PatientResponse> create(PatientRequest patientRequest);

    @Operation(summary = "Buscar pacientes", description = "Permite buscar todos los pacientes, tambien filtrar por DNI exacto o nombre parcial (no sensible a mayúsculas/minúsculas).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes")
    })
    ResponseEntity<List<PatientResponse>> search(String dni, String firstName);

    @Operation(summary = "Buscar paciente por id", description = "Obtiene un paciente específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado")
    })
    ResponseEntity<PatientResponse> findById(Long id);

    @Operation(summary = "Buscar paciente por DNI", description = "Obtiene un paciente específico por su DNI.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado")
    })
    ResponseEntity<PatientResponse> findByDni(String dni);

    @Operation(summary = "Buscar pacientes por nombre", description = "Permite buscar pacientes por nombre parcial (no sensible a mayúsculas/minúsculas).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados"),
            @ApiResponse(responseCode = "204", description = "Pacientes no encontrados")
    })
    ResponseEntity<List<PatientResponse>> searchByFirstName(String firstName);

    @Operation(summary = "Buscar pacientes por seguro de salud", description = "Permite buscar pacientes por seguro de salud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados"),
            @ApiResponse(responseCode = "204", description = "Pacientes no encontrados")
    })
    ResponseEntity<List<PatientResponse>> getByHealthInsurance(
            String healthInsurance, int page, int size);

    @Operation(summary = "Actualizar paciente", description = "Actualiza la información del paciente con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "409", description = "DNI ya existe")
    })
    ResponseEntity<PatientResponse> update(Long id, PatientRequest request);

    @Operation(summary = "Eliminar paciente", description = "Realiza una baja lógica del paciente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente eliminado")
    })
    ResponseEntity<Void> delete(Long id);

    @Operation(summary = "Dar de alta paciente", description = "Da de alta a un paciente previamente dado de baja.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente dado de alta"),
            @ApiResponse(responseCode = "409", description = "El paciente ya está activo"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado")
    })
    ResponseEntity<PatientResponse> activate(Long id);
}
