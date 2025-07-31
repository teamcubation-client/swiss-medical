package microservice.pacientes.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static microservice.pacientes.shared.constants.Constants.APELLIDO_REGEX;
import static microservice.pacientes.shared.constants.Constants.NOMBRE_REGEX;

@AllArgsConstructor
@Getter
@Builder
public class PacienteUpdateDTO {

    @Pattern(regexp = NOMBRE_REGEX, message = "El nombre no puede ser vacío")
    private String nombre;
    @Pattern(regexp = APELLIDO_REGEX, message = "El apellido no puede ser vacío")
    private String apellido;
    private String obraSocial;
    private String email;
    private String telefono;
}
