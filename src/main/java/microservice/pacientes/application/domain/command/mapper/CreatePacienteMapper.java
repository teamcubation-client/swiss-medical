package microservice.pacientes.application.domain.command.mapper;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.factory.PacienteFactory;
import microservice.pacientes.shared.annotations.Mapper;

@AllArgsConstructor
@Mapper
public class CreatePacienteMapper {

    private final PacienteFactory pacienteFactory;
    public Paciente toEntity(CreatePacienteCommand command) {
        return pacienteFactory.create(
                command.getDni(),
                command.getNombre(),
                command.getApellido(),
                command.getObraSocial(),
                command.getEmail(),
                command.getTelefono()
        );
    }

}
