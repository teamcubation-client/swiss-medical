package com.swissmedical.patients.infrastructure.adapter.in.rest.controller;

import java.util.HashMap;
import java.util.List;

import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientResponseDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientUpdateDto;
import com.swissmedical.patients.infrastructure.adapter.in.rest.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.swissmedical.patients.infrastructure.adapter.in.rest.dto.PatientCreateDto;
import com.swissmedical.patients.shared.utils.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Gestión de Pacientes", description = "API para administrar pacientes del sistema")
public interface PatientApi {

  @Operation(summary = "Obtener una lista con todos los pacientes o buscar por nombre")
  @ApiResponse(responseCode = ResponseCode.OK,
          description = "Lista de pacientes obtenida correctamente",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  array = @ArraySchema(schema = @Schema(implementation = PatientResponseDto.class))
          ))
  @ApiResponse(
          responseCode = ResponseCode.NO_CONTENT,
          description = "No se encontraron pacientes con el nombre especificado",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = Void.class)
          ))
  @ApiResponse(
          responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
          description = "Error interno del servidor al obtener la lista de pacientes",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = HashMap.class,
                          description = "Mensaje de error",
                          example = "{\"error\": \"Error interno del servidor\"}"
                  )
          )
  )
  ResponseEntity<BaseResponse<List<PatientResponseDto>>> getAll(String firstName, int limit, int offset);
  @Operation(summary = "Obtener un paciente por su DNI")
  @ApiResponse(
          responseCode = ResponseCode.OK,
          description = "Paciente encontrado",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = PatientResponseDto.class)
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.NO_CONTENT,
          description = "Paciente no encontrado con el DNI especificado",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = Void.class)
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
          description = "Error interno del servidor al obtener el paciente",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = HashMap.class,
                          description = "Mensaje de error",
                          example = "{\"error\": \"Error interno del servidor\"}"
                  )
          )
  )
  ResponseEntity<BaseResponse<PatientResponseDto>> getByDni(String dni);

  @Operation(summary = "Buscar pacientes por nombre de Seguro Social con paginación")
  @ApiResponse(
          responseCode = ResponseCode.OK,
          description = "Lista de pacientes obtenida correctamente",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  array = @ArraySchema(schema = @Schema(implementation = PatientResponseDto.class))
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.NO_CONTENT,
          description = "No se encontraron pacientes con el Seguro Social especificado",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = Void.class)
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
          description = "Error interno del servidor al obtener la lista de pacientes",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = HashMap.class,
                          description = "Mensaje de error",
                          example = "{\"error\": \"Error interno del servidor\"}"
                  )
          )
  )
  ResponseEntity<BaseResponse<List<PatientResponseDto>>> getBySocialSecurity(String socialSecurity, int limit, int offset);

  @Operation(summary = "Crear un nuevo paciente")
  @ApiResponse(
          responseCode = ResponseCode.CREATED,
          description = "Paciente creado correctamente",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = PatientResponseDto.class)
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.BAD_REQUEST,
          description = "Solicitud inválida, datos del paciente incorrectos",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = HashMap.class,
                          description = "Mensaje de error",
                          example = "{\"error\": \"Datos del paciente inválidos\"}"
                  )
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
          description = "Error interno del servidor al crear el paciente",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = HashMap.class,
                          description = "Mensaje de error",
                          example = "{\"error\": \"Error interno del servidor\"}"
                  )
          )
  )
  ResponseEntity<BaseResponse<PatientResponseDto>> create(PatientCreateDto patientCreateDto);

  @Operation(summary = "Actualizar un paciente existente")
  @ApiResponse(
          responseCode = ResponseCode.OK,
          description = "Paciente actualizado correctamente",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = PatientResponseDto.class)
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.BAD_REQUEST,
          description = "Solicitud inválida, datos del paciente incorrectos",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = HashMap.class,
                          description = "Mensaje de error",
                          example = "{\"error\": \"Datos del paciente inválidos\"}"
                  )
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.NO_CONTENT,
          description = "Paciente no encontrado con el ID especificado",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = Void.class)
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
          description = "Error interno del servidor al actualizar el paciente",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = HashMap.class,
                          description = "Mensaje de error",
                          example = "{\"error\": \"Error interno del servidor\"}"
                  )
          )
  )
  ResponseEntity<BaseResponse<PatientResponseDto>> update(PatientUpdateDto patientUpdateDto, Long id);

  @Operation(summary = "Eliminar un paciente por su ID")
  @ApiResponse(
          responseCode = ResponseCode.OK,
          description = "Paciente eliminado correctamente",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = Void.class)
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.NO_CONTENT,
          description = "Paciente no encontrado con el ID especificado",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = Void.class)
          )
  )
  @ApiResponse(
          responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
          description = "Error interno del servidor al eliminar el paciente",
          content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = HashMap.class,
                          description = "Mensaje de error",
                          example = "{\"error\": \"Error interno del servidor\"}"
                  )
          )
  )
  ResponseEntity<Void> delete(Long id);
}
