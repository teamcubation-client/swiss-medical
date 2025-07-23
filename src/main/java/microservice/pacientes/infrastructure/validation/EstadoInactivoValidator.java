package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteActivoException;
import org.springframework.stereotype.Component;

@Component
public class EstadoInactivoValidator implements PacienteValidator {

    private PacienteValidator next;

    @Override
    public void validate(Paciente paciente){
        if (paciente.isEstado()) {
            throw new PacienteActivoException(paciente.getId());
        }
        if (next != null) {
            next.validate(paciente);
        }
    }

    @Override
    public void setNext(PacienteValidator next) {
        this.next = next;
    }
}
