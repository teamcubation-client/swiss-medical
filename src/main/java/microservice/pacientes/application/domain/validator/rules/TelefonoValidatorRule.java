package microservice.pacientes.application.domain.validator.rules;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;

import static microservice.pacientes.shared.constants.Constants.TELEFONO_REGEX;

@AllArgsConstructor
public class TelefonoValidatorRule implements PacienteValidatorRule {

    @Override
    public void validate(Paciente paciente) throws PacienteArgumentoInvalido {
        boolean isInvalid = isInvalid(paciente.getTelefono());
        if (isInvalid)
            throw new PacienteArgumentoInvalido("El teléfono del paciente es inválido");
    }

    private boolean isInvalid(String telefono) {
        return !telefono.matches(TELEFONO_REGEX);
    }
}
