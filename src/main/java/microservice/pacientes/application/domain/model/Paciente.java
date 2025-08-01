package microservice.pacientes.application.domain.model;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class Paciente {
    private long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String obraSocial;
    private String email;
    private String telefono;
    private String tipoPlanObraSocial;
    private java.time.LocalDate fechaAlta;
    private boolean estado;
} 