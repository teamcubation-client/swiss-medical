package microservice.pacientes.infrastructure.adapter.in.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@ToString
public class PacienteDTO {
    private final Long id;
    @NotBlank(message = "El nombre es obligatorio")
    private final String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private final String apellido;
    @NotBlank(message = "El DNI es obligatorio")
    private final String dni;
    @NotBlank(message = "La obra social es obligatoria")
    private final String obraSocial;
    @NotBlank(message = "El correo electrónico es obligatorio")
    private final String email;
    @NotBlank(message = "El teléfono es obligatorio")
    private final String telefono;
    @NotBlank(message = "El tipo de plan de obra social es obligatorio")
    private final String tipoPlanObraSocial;
    @NotNull(message = "La fecha de alta es obligatorio")
    private final java.time.LocalDate fechaAlta;
    @NotNull(message = "El estado es obligatorio")
    private final Boolean estado;
}