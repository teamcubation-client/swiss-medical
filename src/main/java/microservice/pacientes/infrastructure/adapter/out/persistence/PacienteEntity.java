package microservice.pacientes.infrastructure.adapter.out.persistence;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name ="paciente")
public class PacienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
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