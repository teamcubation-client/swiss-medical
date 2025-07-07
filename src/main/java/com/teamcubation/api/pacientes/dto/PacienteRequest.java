package com.teamcubation.api.pacientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Schema(description = "Datos necesarios para crear o actualizar un paciente")
public class PacienteRequest {

    @NotBlank
    @Schema(description = "Nombre del paciente", example = "María", required = true)
    private String nombre;

    @NotBlank
    @Schema(description = "Apellido del paciente", example = "Gómez", required = true)
    private String apellido;

    @NotBlank
    @Schema(description = "Documento Nacional de Identidad", example = "12345678", required = true)
    private String dni;

    @NotBlank
    @Email
    @Schema(description = "Correo electrónico de contacto", example = "maria.gomez@example.com", required = true)
    private String email;

    @Schema(description = "Obra social del paciente (si tiene)", example = "Swiss Medical")
    private String obraSocial;

    @Schema(description = "Teléfono de contacto", example = "12345678")
    private String telefono;

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

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
