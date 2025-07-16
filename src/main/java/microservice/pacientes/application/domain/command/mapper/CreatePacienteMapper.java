package microservice.pacientes.application.domain.command.mapper;

import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;

public class CreatePacienteMapper {

    public static Paciente toEntity(CreatePacienteCommand command) {
        return new Paciente(
                command.getDni(),
                command.getNombre(),
                command.getApellido(),
                command.getObra_social(),
                command.getEmail(),
                command.getTelefono()
        );
    }

}
