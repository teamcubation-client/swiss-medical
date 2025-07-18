package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Schema(description = "Datos necesarios para crear o actualizar un paciente")
public class PatientRequest {

    @NotBlank
    @JsonProperty("nombre")
    @Schema(description = "Nombre del paciente", example = "María", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @JsonProperty("apellido")
    @Schema(description = "Apellido del paciente", example = "Gómez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotBlank
    @Schema(description = "Documento Nacional de Identidad", example = "12345678", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dni;

    @NotBlank
    @Email
    @Schema(description = "Correo electrónico de contacto", example = "maria.gomez@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @JsonProperty("obraSocial")
    @Schema(description = "Obra social del paciente (si tiene)", example = "Swiss Medical")
    private String healthInsuranceProvider;

    @JsonProperty("telefono")
    @Schema(description = "Teléfono de contacto", example = "12345678")
    private String phoneNumber;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHealthInsuranceProvider() {
        return healthInsuranceProvider;
    }

    public void setHealthInsuranceProvider(String healthInsuranceProvider) {
        this.healthInsuranceProvider = healthInsuranceProvider;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}