package com.tq.pacientes.documentation;

import com.tq.pacientes.dtos.PatientDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Pacientes", description = "API de Pacientes")
public interface IPacienteController {

    @Operation(summary = "Crear un paciente", description = "Registra un nuevo paciente en el sistema. El DNI debe ser único. Si el DNI ya existe, se devuelve un error 409.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado"),
            @ApiResponse(responseCode = "409", description = "Paciente duplicado"),
    })
    ResponseEntity<PatientDTO> create(PatientDTO patientDTO);

    @Operation(summary = "Buscar pacientes", description = "Permite buscar todos los pacientes, tambien filtrar por DNI exacto o nombre parcial (no sensible a mayúsculas/minúsculas).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes")
    })
    ResponseEntity<List<PatientDTO>> search(String dni, String firstName);

    @Operation(summary = "Buscar paciente por id", description = "Obtiene un paciente específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado")
    })
    ResponseEntity<PatientDTO> findById(Long id);

    @Operation(summary = "Actualizar paciente", description = "Actualiza la información del paciente con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "409", description = "DNI ya existe")
    })
    ResponseEntity<PatientDTO> update(Long id, PatientDTO dto);

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
    ResponseEntity<Void> activate(Long id);
}
