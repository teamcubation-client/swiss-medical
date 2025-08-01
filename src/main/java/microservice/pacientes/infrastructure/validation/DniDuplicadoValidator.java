package microservice.pacientes.infrastructure.validation;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteDuplicadoException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class DniDuplicadoValidator implements PacienteValidator {
    private PacienteValidator next;
    private final PacientePortOutRead pacientePortOutRead;

    public DniDuplicadoValidator(PacientePortOutRead pacientePortOutRead) {
        this.pacientePortOutRead = pacientePortOutRead;
    }

    @Override
    public void validate(Paciente paciente){
        pacientePortOutRead.buscarByDni(paciente.getDni())
                .filter(existente -> !Objects.equals(existente.getId(), paciente.getId()))
                .ifPresent(existente -> {
                    throw new PacienteDuplicadoException(paciente.getDni());
                });
        if (next != null) {
            next.validate(paciente);
        }

    }

    @Override
    public void setNext(PacienteValidator next) {
        this.next = next;
    }

}
