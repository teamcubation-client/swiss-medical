package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.InvalidFechaAltaException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class FechaAltaValidator implements PacienteValidator {

    private PacienteValidator next;

    public FechaAltaValidator() {
    }


    @Override
    public void validate(Paciente paciente) {
        LocalDate alta = paciente.getFechaAlta();
        if (alta != null && alta.isAfter(LocalDate.now())) {
            throw new InvalidFechaAltaException(alta);
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
