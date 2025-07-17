package microservice.pacientes.application.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import microservice.pacientes.application.domain.validator.PacienteValidator;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;

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
        if(!PacienteValidator.isValidDni(dni)) {
            throw new PacienteArgumentoInvalido("El DNI es inválido");
        }
        if(!PacienteValidator.isValidNombre(nombre)) {
            throw new PacienteArgumentoInvalido("El nombre es inválido");
        }
        if(!PacienteValidator.isValidApellido(apellido)) {
            throw new PacienteArgumentoInvalido("El apellido es inválido");
        }
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Paciente(String dni, String nombre, String apellido, String obraSocial, String email, String telefono) {
        if(!PacienteValidator.isValidDni(dni)) {
            throw new PacienteArgumentoInvalido("El DNI es inválido");
        }
        if(!PacienteValidator.isValidNombre(nombre)) {
            throw new PacienteArgumentoInvalido("El nombre es inválido");
        }
        if(!PacienteValidator.isValidApellido(apellido)) {
            throw new PacienteArgumentoInvalido("El apellido es inválido");
        }
        if(!PacienteValidator.isValidEmail(email)) {
            throw new PacienteArgumentoInvalido("El email es inválido");
        }
        if(!PacienteValidator.isValidTelefono(telefono)) {
            throw new PacienteArgumentoInvalido("El teléfono es inválido");
        }
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.obraSocial = obraSocial;
        this.email = email;
        this.telefono = telefono;
    }


}
