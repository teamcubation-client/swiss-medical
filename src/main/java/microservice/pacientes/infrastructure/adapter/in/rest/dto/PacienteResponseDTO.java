package microservice.pacientes.infrastructure.adapter.in.rest.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PacienteResponseDTO {
    private String dni;
    private String nombre;
    private String apellido;
    private String obraSocial;
}
