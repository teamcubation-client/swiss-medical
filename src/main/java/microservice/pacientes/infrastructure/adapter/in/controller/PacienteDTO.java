package microservice.pacientes.infrastructure.adapter.in.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object para transferir informacion de pacientes entre capas de la aplicacion
 * Incluye datos personales, de contacto y la obra social del paciente
 */
@Data
@Builder
public class PacienteDTO {
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;
    @NotBlank(message = "La obra social es obligatoria")
    private String obraSocial;
    @NotBlank(message = "El correo electrónico es obligatorio")
    private String email;
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
    @NotBlank(message = "El tipo de plan de obra social es obligatorio")
    private String tipoPlanObraSocial;
    @NotNull(message = "La fecha de alta es obligatorio")
    private java.time.LocalDate fechaAlta;
    private boolean estado;
} 