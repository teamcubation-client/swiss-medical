package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ExistePacienteValidator implements PacienteValidator {

    private final PacientePortOutRead pacientePortOutRead;
    private final LoggerPort logger;
    private PacienteValidator next;

    public ExistePacienteValidator(PacientePortOutRead pacientePortOutRead, LoggerPort logger) {
        this.pacientePortOutRead = pacientePortOutRead;
        this.logger = logger;
    }
    
    @Override
    public void validate(Paciente paciente) {
        Long id = paciente.getId();
        logger.info("[ExistePacienteValidator] Verificando existencia del paciente con ID={}", id);
        if (id == null || pacientePortOutRead.findById(id).isEmpty()) {
            logger.error("[ExistePacienteValidator] Paciente con ID={} no existe en la base de datos", id);
            throw PacienteNotFoundException.porId(id);
        }
        logger.info("[ExistePacienteValidator] Paciente con ID={} existe", id);
        if (next != null) {
            next.validate(paciente);
        }
    }

    @Override
    public void setNext(PacienteValidator next) {
        this.next = next;
    }
}
