package microservice.pacientes.infrastructure.adapter.out.persistence;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Entidad que representa a un paciente
 * Contiene informacion personal y de contacto, ademas de la obra social al que pertenece el paciente
 */
@Data
@Entity
@NoArgsConstructor
@Table (name ="paciente")
public class PacienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

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