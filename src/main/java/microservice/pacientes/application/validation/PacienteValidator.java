package microservice.pacientes.application.validation;

import microservice.pacientes.application.domain.model.Paciente;

public interface PacienteValidator {
    void validate (Paciente paciente);
    void setNext (PacienteValidator next);
}
