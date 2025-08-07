package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.InvalidEmailFormatException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailFormatValidator implements PacienteValidator {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private final LoggerPort logger;
    private PacienteValidator next;

    public EmailFormatValidator(LoggerPort logger) {
        this.logger = logger;
    }

    @Override
    public void validate(Paciente paciente) {
        String email = paciente.getEmail();
        logger.info("[EmailFormatValidator] Validando formato de email: {}", email);
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            logger.error("[EmailFormatValidator] Formato de email invalido: {}", email);
            throw new InvalidEmailFormatException(email);
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
