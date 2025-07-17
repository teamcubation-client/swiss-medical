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
    /** Identificador unico del paciente */
    private long id;
    /** Nombre del paciente */
    private String nombre;
    /** Apellido del paciente */
    private String apellido;
    /** Documento Nacional de Identidad del paciente */
    private String dni;
    /** Obra social a la que pertenece el paciente */
    private String obraSocial;
    /** Correo electronico del paciente */
    private String email;
    /** Telefono de contacto del paciente */
    private String telefono;
    /** Tipo de plan de la obra social */
    private String tipoPlanObraSocial;
    /** Fecha de alta del paciente en el sistema */
    private java.time.LocalDate fechaAlta;
    /** Estado del paciente (true=activo, false=inactivo) */
    private boolean estado;

} 