package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteDuplicadoException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class DniDuplicadoValidator implements PacienteValidator {

    private final PacientePortOutRead pacientePortOutRead;
    private final LoggerPort logger;
    private PacienteValidator next;

    public DniDuplicadoValidator(PacientePortOutRead pacientePortOutRead, LoggerPort logger) {
        this.pacientePortOutRead = pacientePortOutRead;
        this.logger = logger;
    }

    @Override
    public void validate(Paciente paciente){
        logger.info("[DniDuplicadoValidator] Validando DNI duplicado para: {}", paciente.getDni());
        Optional<Paciente> existente = pacientePortOutRead.buscarByDni(paciente.getDni());
        
        if (existente.isPresent() && !Objects.equals(existente.get().getId(), paciente.getId())) {
            logger.error("[DniDuplicadoValidator] DNI duplicado detectado: {}", paciente.getDni());
            throw new PacienteDuplicadoException(paciente.getDni());
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
