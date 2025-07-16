package microservice.pacientes.infrastructure.adapter.in.rest.dto.mapper;

import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.PacienteRequestDTO;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.PacienteUpdateDTO;

public class PacienteRequestMapper {

    public static CreatePacienteCommand toCreateCommand(PacienteRequestDTO dto) {
        return new CreatePacienteCommand(
                dto.getDni(),
                dto.getNombre(),
                dto.getApellido(),
                dto.getObra_social(),
                dto.getEmail(),
                dto.getTelefono()
        );
    }

    public static UpdatePacienteCommand toUpdateCommand(PacienteUpdateDTO dto) {
        return new UpdatePacienteCommand(
                dto.getNombre(),
                dto.getApellido(),
                dto.getObra_social(),
                dto.getEmail(),
                dto.getTelefono()
        );
    }


}
