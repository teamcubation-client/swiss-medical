package microservice.pacientes.application.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Paciente {
    private String dni;
    private String nombre;
    private String apellido;
    private String obraSocial;
    private String email;
    private String telefono;
    public Paciente(String dni, String nombre, String apellido) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }
    public Paciente(String dni, String nombre, String apellido, String obraSocial, String email, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.obraSocial = obraSocial;
        this.email = email;
        this.telefono = telefono;
    }
}
