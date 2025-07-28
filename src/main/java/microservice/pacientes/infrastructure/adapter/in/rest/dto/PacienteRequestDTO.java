package microservice.pacientes.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PacienteRequestDTO {
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    private String obraSocial;
    private String email;
    private String telefono;
}
