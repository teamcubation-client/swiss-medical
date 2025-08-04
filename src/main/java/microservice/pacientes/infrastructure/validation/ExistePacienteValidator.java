package microservice.pacientes.infrastructure.validation;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExistePacienteValidator implements PacienteValidator {

    private static final Logger logger = LoggerFactory.getLogger(ExistePacienteValidator.class);
    
    private final PacientePortOutRead pacientePortOutRead;
    private PacienteValidator next;

    public ExistePacienteValidator(PacientePortOutRead pacientePortOutRead) {
        this.pacientePortOutRead = pacientePortOutRead;
    }
    
    @Override
    public void validate(Paciente paciente) {
        Long id = paciente.getId();
        logger.debug("Validando existencia de paciente con ID: {}", id);
        
        if (id == null || pacientePortOutRead.findById(id).isEmpty()) {
            logger.warn("Paciente no encontrado con ID: {}", id);
            throw PacienteNotFoundException.porId(id);
        }
        
        logger.debug("Paciente encontrado exitosamente con ID: {}", id);

        if (next != null) {
            next.validate(paciente);
        }
    }

    @Override
    public void setNext(PacienteValidator next) {
        this.next = next;
    }
}
