package microservice.pacientes.application.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import microservice.pacientes.application.domain.validator.PacienteValidator;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;

@Getter
@Setter
@Builder
public class UpdatePacienteCommand {
    private final String nombre;
    private final String apellido;
    private final String obraSocial;
    private final String email;
    private final String telefono;

    public UpdatePacienteCommand(String nombre, String apellido, String obraSocial, String email, String telefono) {
        if(nombre != null && !PacienteValidator.isValidNombre(nombre)) {
            throw new PacienteArgumentoInvalido("El nombre es inválido");
        }
        if(apellido != null && !PacienteValidator.isValidApellido(apellido)) {
            throw new PacienteArgumentoInvalido("El apellido es inválido");
        }
        if(email != null && !PacienteValidator.isValidEmail(email)) {
            throw new PacienteArgumentoInvalido("El email es inválido");
        }
        if(telefono != null && !PacienteValidator.isValidTelefono(telefono)) {
            throw new PacienteArgumentoInvalido("El teléfono es inválido");
        }
        this.nombre = nombre;
        this.apellido = apellido;
        this.obraSocial = obraSocial;
        this.email = email;
        this.telefono = telefono;
    }
}
