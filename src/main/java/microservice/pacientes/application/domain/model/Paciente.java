package microservice.pacientes.application.domain.model;
import lombok.Builder;
import lombok.Data;

/**
 * Modelo que representa a un paciente
 * Contiene informacion personal y de contacto, ademas de la obra social al que pertenece el paciente
 */
@Data
@Builder
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