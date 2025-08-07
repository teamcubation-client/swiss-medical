package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.InvalidFechaAltaException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FechaAltaValidator implements PacienteValidator {
    private final LoggerPort logger;
    private PacienteValidator next;

    public FechaAltaValidator(LoggerPort logger) {
        this.logger = logger;
    }


    @Override
    public void validate(Paciente paciente) {
        LocalDate alta = paciente.getFechaAlta();
        logger.info("[FechaAltaValidator] Validando fecha de alta: {}", alta);
        if (alta != null && alta.isAfter(LocalDate.now())) {
            logger.error("[FechaAltaValidator] Fecha de alta invalida (futura): {}", alta);
            throw new InvalidFechaAltaException(alta);
        }
        logger.info("[FechaAltaValidator] Fecha de alta valida: {}", alta);
        if (next != null) {
            next.validate(paciente);
        }
    }

    @Override
    public void setNext(PacienteValidator next) {
        this.next = next;
    }
}
