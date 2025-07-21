package microservice.pacientes.application.domain.validator;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.validator.rules.PacienteValidatorRule;

import java.util.List;

@AllArgsConstructor
public class PacienteValidatorImpl implements PacienteValidator {
    private List<PacienteValidatorRule> rules;

    public void validate(Paciente paciente) {
        rules.forEach(rule -> rule.validate(paciente));
    }
}