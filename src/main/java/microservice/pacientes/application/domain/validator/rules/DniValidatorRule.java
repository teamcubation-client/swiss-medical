package microservice.pacientes.application.domain.validator.rules;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;

import static microservice.pacientes.shared.constants.Constants.DNI_REGEX;

@AllArgsConstructor
public class DniValidatorRule implements PacienteValidatorRule {

    @Override
    public void validate(Paciente paciente) throws PacienteArgumentoInvalido {
        boolean isInvalid = isInvalid(paciente.getDni());
        if (isInvalid)
            throw new PacienteArgumentoInvalido("El DNI del paciente es inválido");
    }

    private boolean isInvalid(String dni) {
        return !dni.matches(DNI_REGEX);
    }
}
