package microservice.pacientes.infrastructure.adapter.in.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PacienteResponseDTO {
    private String dni;
    private String nombre;
    private String apellido;
    private String obraSocial;
}
