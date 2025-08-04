package microservice.pacientes.application.domain.validator.rules;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;

import static microservice.pacientes.shared.constants.Constants.APELLIDO_REGEX;

@AllArgsConstructor
public class ApellidoValidatorRule implements PacienteValidatorRule {

    @Override
    public void validate(Paciente paciente) throws PacienteArgumentoInvalido {
        boolean isInvalid = isInvalid(paciente.getApellido());
        if (isInvalid)
            throw new PacienteArgumentoInvalido("El apellido del paciente es inválido");
    }

    private boolean isInvalid(String apellido) {
        return !apellido.matches(APELLIDO_REGEX);
    }
}
