package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ExistePacienteValidator implements PacienteValidator {

    private final PacientePortOutRead pacientePortOutRead;
    private PacienteValidator next;

    public ExistePacienteValidator(PacientePortOutRead pacientePortOutRead) {
        this.pacientePortOutRead = pacientePortOutRead;
    }
    
    @Override
    public void validate(Paciente paciente) {
        Long id = paciente.getId();
        
        if (id == null || pacientePortOutRead.findById(id).isEmpty()) {
            throw PacienteNotFoundException.porId(id);
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
