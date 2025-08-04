package microservice.pacientes.infrastructure.validation;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteDuplicadoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class DniDuplicadoValidator implements PacienteValidator {
    private static final Logger logger = LoggerFactory.getLogger(DniDuplicadoValidator.class);
    
    private PacienteValidator next;
    private final PacientePortOutRead pacientePortOutRead;

    public DniDuplicadoValidator(PacientePortOutRead pacientePortOutRead) {
        this.pacientePortOutRead = pacientePortOutRead;
    }

    @Override
    public void validate(Paciente paciente){
        logger.debug("Validando DNI duplicado para paciente con DNI: {}", paciente.getDni());
        
        Optional<Paciente> existente = pacientePortOutRead.buscarByDni(paciente.getDni());
        
        if (existente.isPresent() && !Objects.equals(existente.get().getId(), paciente.getId())) {
            logger.warn("DNI duplicado detectado: {} - Paciente existente ID: {}, Paciente actual ID: {}", 
                       paciente.getDni(), existente.get().getId(), paciente.getId());
            throw new PacienteDuplicadoException(paciente.getDni());
        }
        
        logger.debug("Validacion de DNI duplicado completada exitosamente para DNI: {}", paciente.getDni());
        
        if (next != null) {
            next.validate(paciente);
        }
    }

    @Override
    public void setNext(PacienteValidator next) {
        this.next = next;
    }
}
