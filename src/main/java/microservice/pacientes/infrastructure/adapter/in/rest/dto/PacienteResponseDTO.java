package microservice.pacientes.infrastructure.adapter.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PacienteResponseDTO {
    private String dni;
    private String nombre;
    private String apellido;
    private String obraSocial;
}
