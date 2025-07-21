package microservice.pacientes.application.domain.validator;

import microservice.pacientes.application.domain.model.Paciente;

public interface PacienteValidator {
    void validate(Paciente paciente);
}
