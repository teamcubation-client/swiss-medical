package microservice.pacientes.infrastructure.adapter.in.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object para transferir informacion de pacientes entre capas de la aplicacion
 * Incluye datos personales, de contacto y la obra social del paciente
 */
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
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
    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}