package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.controller;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

@Validated
@Tag(name = "Pacientes", description = "Operaciones relacionadas con pacientes")
public interface IPatientAPI {

    @Operation(summary = "Crear un nuevo paciente")
    ResponseEntity<ApiResponse<PatientResponse>> create(@Valid PatientRequest request);

    @Operation(summary = "Obtener todos los pacientes")
    ResponseEntity<ApiResponse<List<PatientResponse>>> getAll(
            @Parameter(description = "DNI completo del paciente", example = "12345678")
            @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos numéricos")
            String dni,
            @Parameter(description = "Nombre completo o parcial del paciente", example = "Roberto")
            @Pattern(
                    regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$",
                    message = "El nombre solo puede contener letras, espacios y tildes"
            )
            String name
    );

    @Operation(summary = "Buscar paciente por ID")
    ResponseEntity<ApiResponse<PatientResponse>> getById(
            @Parameter(description = "ID único del paciente", example = "1")
            @Min(1)
            long id
    );

    @Operation(summary = "Actualizar un paciente existente")
    ResponseEntity<ApiResponse<PatientResponse>> updateById(
            @Parameter(description = "ID único del paciente", example = "1")
            @Min(1)
            long id,
            @Valid
            PatientRequest request
    );

    @Operation(summary = "Eliminar un paciente por ID")
    ResponseEntity<Void> deleteById(
            @Parameter(description = "ID único del paciente", example = "1")
            @Min(1)
            long id
    );

    @Operation(summary = "Buscar paciente por DNI")
    ResponseEntity<ApiResponse<PatientResponse>> getByDni(
            @Parameter(description = "DNI completo del paciente", example = "12345678")
            @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos numéricos")
            String dni
    );

    @Operation(summary = "Buscar paciente por nombre")
    ResponseEntity<ApiResponse<List<PatientResponse>>> getByName(
            @Parameter(description = "Nombre parcial o completo del paciente", example = "Ana")
            @Pattern(
                    regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+$",
                    message = "El nombre solo puede contener letras, espacios y tildes"
            )
            String name
    );

    @Operation(summary = "Buscar paciente por obra social")
    ResponseEntity<ApiResponse<List<PatientResponse>>> getByHealthInsuranceProvider(
            @Parameter(description = "Nombre parcial o completo de obra social", example = "Swiss Medical")
            @Pattern(
                    regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ\\s]+$",
                    message = "El nombre de la obra social solo puede contener letras, números, espacios y tildes"
            )
            String healthInsuranceProvider,
            @Parameter(description = "Número de página (0 basado)", example = "0")
            @Min(0)
            int page,
            @Parameter(description = "Cantidad de elementos por página", example = "10")
            @Min(5)
            int size
    );

    @Operation(summary = "Permite exportar lista de pacientes en el formato seleccionado")
    ResponseEntity<ApiResponse<String>> export(@Parameter(description = "Formato elegido", example = "csv") String format);
}
