package microservice.pacientes.application.domain.validator.rules;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;

@AllArgsConstructor
public class EmailValidatorRule implements PacienteValidatorRule {
    @Override
    public void validate(Paciente paciente) throws PacienteArgumentoInvalido {
        boolean isInvalid = isInvalid(paciente.getEmail());
        if (isInvalid)
            throw new PacienteArgumentoInvalido("El email del paciente es inválido");
    }

    private boolean isInvalid(String email) {
        return !email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }
}
