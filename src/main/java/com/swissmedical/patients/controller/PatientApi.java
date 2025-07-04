package com.swissmedical.patients.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

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
    public ResponseEntity<List<Patient>> getMethodName(String name) throws Exception;
}
