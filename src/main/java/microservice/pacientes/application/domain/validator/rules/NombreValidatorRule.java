package microservice.pacientes.application.domain.validator.rules;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.validator.PacienteValidatorRule;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;

@AllArgsConstructor
public class NombreValidatorRule implements PacienteValidatorRule {
    @Override
    public void validate(Paciente paciente) throws PacienteArgumentoInvalido {
        boolean isInvalid = isInvalid(paciente.getNombre());
        if (isInvalid)
            throw new PacienteArgumentoInvalido("El nombre del paciente es inválido");
    }

    private boolean isInvalid(String nombre) {
        return !nombre.matches("^[\\p{L}\\s]+$");
    }
}
