package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteActivoException;
import org.springframework.stereotype.Component;

@Component
public class EstadoInactivoValidator implements PacienteValidator {

    private final LoggerPort logger;
    private PacienteValidator next;

    public EstadoInactivoValidator(LoggerPort logger) {
        this.logger = logger;
    }

    @Override
    public void validate(Paciente paciente){
        logger.info("[EstadoInactivoValidator] Validando que el paciente este inactivo. ID={}", paciente.getId());
        if (paciente.isEstado()) {
            logger.error("[EstadoInactivoValidator] El paciente con ID={} esta activo y no puede ser eliminado", paciente.getId());
            throw new PacienteActivoException(paciente.getId());
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
