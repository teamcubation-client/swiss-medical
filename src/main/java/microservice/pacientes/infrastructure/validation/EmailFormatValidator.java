package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.InvalidEmailFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailFormatValidator implements PacienteValidator {

    private static final Logger logger = LoggerFactory.getLogger(EmailFormatValidator.class);
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private PacienteValidator next;

    @Override
    public void validate(Paciente paciente) {
        String email = paciente.getEmail();
        logger.debug("Validando formato de email: {}", email);
        
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            logger.warn("Formato de email invalido: {}", email);
            throw new InvalidEmailFormatException(email);
        }
        
        logger.debug("Formato de email valido: {}", email);
        
        if (next != null) {
            next.validate(paciente);
        }
    }

    @Override
    public void setNext(PacienteValidator next) {
        this.next = next;
    }
}
