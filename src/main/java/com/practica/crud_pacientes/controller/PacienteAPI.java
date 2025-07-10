package com.practica.crud_pacientes.controller;

import com.practica.crud_pacientes.dto.PacienteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Paciente")
public interface PacienteAPI {

    @Operation(
            summary = "Listar todos los pacientes",
            description = "Devuelve una lista con todos los pacientes registrados en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operación exitosa"),
                    @ApiResponse(responseCode = "400", description = "Error en la solicitud")
            }
    )
    ResponseEntity<List<PacienteDto>> getPacientes();

    @Operation(
            summary = "Buscar paciente por ID",
            description = "Devuelve el paciente correspondiente al ID proporcionado. Si no existe, se lanza una excepción.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
                    @ApiResponse(responseCode = "404", description = "Paciente inexistente")
            }
    )
    ResponseEntity<PacienteDto> getPaciente(@PathVariable int id);

    @Operation(
            summary = "Buscar paciente por DNI",
            description = "Devuelve el paciente correspondiente al DNI proporcionado. Si no existe o el formato es incorrecto, se lanza una excepción.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
                    @ApiResponse(responseCode = "404", description = "Paciente inexistente")
            }
    )
    ResponseEntity<PacienteDto> getPacienteByDni(@RequestParam String dni);

    @Operation(
            summary = "Buscar pacientes por nombre",
            description = "DDevuelve una lista de pacientes cuyos nombres coincidan parcialmente con el valor ingresado, sin distinguir entre mayúsculas, minúsculas ni tildes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operacion exitosa")
            }
    )
    ResponseEntity<List<PacienteDto>> getPacienteByName(@RequestParam String nombre);

    @Operation(
            summary = "Registrar un nuevo paciente",
            description = "Crea un nuevo paciente en el sistema. El DNI debe ser único. En caso de duplicado, se lanza una excepción.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Paciente creado con exito"),
                    @ApiResponse(responseCode = "409", description = "Ya existe un paciente con ese DNI")
            }
    )
    ResponseEntity<PacienteDto> addPaciente(@RequestBody PacienteDto pacienteNuevo);

    @Operation(
            summary = "Actualizar un paciente existente",
            description = "Modifica los datos del paciente identificado por su ID. En caso de conflicto con el DNI u otros errores, se lanza una excepción.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paciente actualizado con exito"),
                    @ApiResponse(responseCode = "409", description = "Ya existe un paciente con ese DNI"),
                    @ApiResponse(responseCode = "400", description = "Los datos que se intentan actualizar son incorrectos")
            }
    )
    ResponseEntity<PacienteDto> updatePaciente(@PathVariable int id, @RequestBody PacienteDto paciente);

    @Operation(
            summary = "Elimina un paciente",
            description = "Elimina el paciente correspondiente al ID proporcionado. Si no existe, se lanza una excepción.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paciente eliminado con exito"),
                    @ApiResponse(responseCode = "404", description = "El paciente que se intenta eliminar no existe")
            }
    )
    ResponseEntity<Void> deletePaciente(@PathVariable int id);
}
