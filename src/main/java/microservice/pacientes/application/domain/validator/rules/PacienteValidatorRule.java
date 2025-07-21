package microservice.pacientes.application.domain.validator.rules;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;

public interface PacienteValidatorRule {
    void validate(Paciente paciente) throws PacienteArgumentoInvalido;
}
