package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.InvalidFechaAltaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class FechaAltaValidator implements PacienteValidator {

    private PacienteValidator next;
    private static final Logger logger = LoggerFactory.getLogger(FechaAltaValidator.class);

    public FechaAltaValidator() {
    }


    @Override
    public void validate(Paciente paciente) {
        LocalDate alta = paciente.getFechaAlta();
        logger.debug("Validando fecha de alta para paciente con ID: {}, Fecha: {}", paciente.getId(), alta);

        if (alta != null && alta.isAfter(LocalDate.now())) {
            logger.warn("Fecha de alta invalida para paciente ID {}: {}", paciente.getId(), alta);
            throw new InvalidFechaAltaException(alta);
        }
        logger.debug("Fecha de alta valida para paciente ID: {}", paciente.getId());
        if (next != null) {
            next.validate(paciente);
        }
    }

    @Override
    public void setNext(PacienteValidator next) {
        this.next = next;
    }
}
