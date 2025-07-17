package microservice.pacientes.infrastructure.adapter.in.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import lombok.Data;

/**
 * Data Transfer Object para transferir informacion de pacientes entre capas de la aplicacion
 * Incluye datos personales, de contacto y la obra social del paciente
 */
@Data
public class PacienteDTO {
    /** Identificador unico del paciente */
    private Long id;
    /** Nombre del paciente */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    /** Apellido del paciente */
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    /** Documento Nacional de Identidad del paciente */
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;
    /** Obra social a la que pertenece el paciente */
    @NotBlank(message = "La obra social es obligatoria")
    private String obraSocial;
    /** Correo electronico del paciente */
    @NotBlank(message = "El correo electrónico es obligatorio")
    private String email;
    /** Telefono de contacto del paciente */
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
    /** Tipo de plan de la obra social */
    @NotBlank(message = "El tipo de plan de obra social es obligatorio")
    private String tipoPlanObraSocial;
    /** Fecha de alta del paciente en el sistema */
    @PastOrPresent(message = "La fecha de alta no puede ser futura")
    private java.time.LocalDate fechaAlta;
    /** Estado del paciente (true=activo, false=inactivo) */
    private boolean estado;
} 