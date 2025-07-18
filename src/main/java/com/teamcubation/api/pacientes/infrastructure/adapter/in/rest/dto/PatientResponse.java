package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con datos de un paciente")
public class PatientResponse {

    @Schema(description = "Identificador único del paciente", example = "123")
    private Long id;

    @Schema(description = "Nombre del paciente", example = "María")
    private String name;

    @Schema(description = "Apellido del paciente", example = "Gómez")
    private String lastName;

    @Schema(description = "Documento Nacional de Identidad", example = "12345678")
    private String dni;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
