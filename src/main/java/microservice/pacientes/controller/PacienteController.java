package microservice.pacientes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import microservice.pacientes.controller.dto.PacienteRequestDTO;
import microservice.pacientes.controller.dto.PacienteResponseDTO;
import microservice.pacientes.controller.dto.PacienteUpdateDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface PacienteController {

    @Operation(
            summary = "Obtener todos los pacientes",
            description = "Devuelve los datos de todos los pacientes registrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados. Incluye lista vacía."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<List<PacienteResponseDTO>> getAll(
            @Parameter(
                    description = "Nombre parcial o completo para filtrar los pacientes. Opcional.",
                    required = false
            )
            String nombre
    );

    @Operation(
            summary = "Obtener un paciente según su DNI",
            description = "Devuelve los datos del paciente buscado por DNI"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<PacienteResponseDTO> getByDni(
            @Parameter(
                    description = "DNI del paciente",
                    required = true
            )
            String dni
    );

    @Operation(
            summary = "Crea un nuevo paciente",
            description = "Devuelve los datos del paciente creado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado"),
            @ApiResponse(responseCode = "409", description = "Paciente duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<PacienteResponseDTO> create(PacienteRequestDTO pacienteRequestDTO);

    @Operation(
            summary = "Actualiza parcial o totalmente un paciente",
            description = "Devuelve los datos del paciente actualizado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente actualizado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<PacienteResponseDTO> update(String dni, PacienteUpdateDTO pacienteUpdateDTO);

    @Operation(
            summary = "Elimina un paciente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente eliminado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<Void> delete(String dni);

    @Operation(
            summary = "Obtener un paciente según su DNI usando SP",
            description = "Devuelve los datos del paciente buscado por DNI"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<PacienteResponseDTO> getByDniSP(String dni);

    @Operation(
            summary = "Obtener un paciente según su nombre usando SP",
            description = "Devuelve los datos del paciente buscado por DNI"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "204", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<PacienteResponseDTO> getByNombreSP(String nombre);

    @Operation(
            summary = "Obtener todos los pacientes según cierta obra social usando SP y paginación",
            description = "Devuelve los datos de todos los pacientes con cierta obra social"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados. Incluye lista vacía."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<List<PacienteResponseDTO>> getByObraSocialSP(
            @Parameter(
                    description = "Obra social",
                    required = true
            )
            String obraSocial,
            @Parameter(
                    description = "Página",
                    required = true
            )
            int page,
            @Parameter(
                    description = "Tamaño de la página",
                    required = true
            )
            int size);



}