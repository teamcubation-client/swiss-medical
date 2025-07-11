package microservice.pacientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedStoredProcedureQuery(
        name = "findByNombreSP",
        procedureName = "buscar_pacientes_por_nombre",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_nombre", type = String.class)
        },
        resultClasses = Paciente.class
)
@NamedStoredProcedureQuery(
        name = "findByObraSocialSP",
        procedureName = "buscar_pacientes_por_obra_social_paginado",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_obra_social", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_limit", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_offset", type = Integer.class)
        },
        resultClasses = Paciente.class
)
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Paciente {

    @Id
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    private String obra_social;

    private String email;

    private String telefono;

    public Paciente(String dni, String nombre, String apellido, String obra_social, String email, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.obra_social = obra_social;
        this.email = email;
        this.telefono = telefono;
    }

}
