package microservice.pacientes.infrastructure.adapter.out.persistence.mysql.updater;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacienteUpdater;
import microservice.pacientes.application.domain.validator.PacienteValidator;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PacienteUpdaterImpl implements PacienteUpdater {
    private final MapStructPacienteUpdater mapStructPacienteUpdater;
    private final PacienteValidator pacienteValidator;

    @Override
    public void update(UpdatePacienteCommand command, Paciente paciente) {
        mapStructPacienteUpdater.update(command, paciente);
        pacienteValidator.validate(paciente);
    }
}