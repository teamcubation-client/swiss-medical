package com.tq.pacientes.documentation;

import com.tq.pacientes.dtos.ActualizarPacienteDTO;
import com.tq.pacientes.dtos.PacienteDTO;
import com.tq.pacientes.models.Paciente;
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
    ResponseEntity<PacienteDTO> crear(Paciente paciente);

    @Operation(summary = "Buscar pacientes", description = "Permite buscar todos los pacientes, tambien filtrar por DNI exacto o nombre parcial (no sensible a mayúsculas/minúsculas).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados"),
            @ApiResponse(responseCode = "204", description = "Pacientes no encontrados")
    })
    ResponseEntity<List<PacienteDTO>> buscar(
            String dni,
            String nombre
    );

    @Operation(summary = "Buscar paciente por id", description = "Obtiene un paciente específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado")
    })
    ResponseEntity<PacienteDTO> buscarPorId(Long id);

    @Operation(summary = "Actualizar paciente", description = "Actualiza la información del paciente con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado")
    })
    public ResponseEntity<PacienteDTO> actualizar(Long id, ActualizarPacienteDTO dto);

    @Operation(summary = "Eliminar paciente", description = "Realiza una baja lógica del paciente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente eliminado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado")
    })
    ResponseEntity<Void> eliminar(Long id);

}
