package microservice.pacientes.application.domain.command.mapper;

import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;

public class CreatePacienteMapper {

    public static Paciente toEntity(CreatePacienteCommand command) {
        return Paciente.builder()
                .dni(command.getDni())
                .nombre(command.getNombre())
                .apellido(command.getApellido())
                .obraSocial(command.getObraSocial())
                .email(command.getEmail())
                .telefono(command.getTelefono())
                .build();
    }

}
