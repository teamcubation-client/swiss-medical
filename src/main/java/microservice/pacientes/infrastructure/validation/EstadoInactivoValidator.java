package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteActivoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EstadoInactivoValidator implements PacienteValidator {

    private PacienteValidator next;
    private static final Logger logger = LoggerFactory.getLogger(EstadoInactivoValidator.class);

    @Override
    public void validate(Paciente paciente){
        logger.debug("Validando estado inactivo para paciente con ID: {}", paciente.getId());
        if (paciente.isEstado()) {
            logger.warn("Paciente con ID {} activo y no puede ser procesado como inactivo", paciente.getId());
            throw new PacienteActivoException(paciente.getId());
        }

        logger.debug("Paciente con ID {} está inactivo. Validacion completada exitosamente.", paciente.getId());
        if (next != null) {
            next.validate(paciente);
        }
    }

    @Override
    public void setNext(PacienteValidator next) {
        this.next = next;
    }
}
