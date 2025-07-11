package com.teamcubation.api.pacientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con datos de un paciente")
public class PacienteResponse {

    @Schema(description = "Identificador único del paciente", example = "123")
    private Long id;

    @Schema(description = "Nombre del paciente", example = "María")
    private String nombre;

    @Schema(description = "Apellido del paciente", example = "Gómez")
    private String apellido;

    @Schema(description = "Documento Nacional de Identidad", example = "12345678")
    private String dni;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
