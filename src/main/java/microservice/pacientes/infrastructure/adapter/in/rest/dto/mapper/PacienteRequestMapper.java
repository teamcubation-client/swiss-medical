package microservice.pacientes.infrastructure.adapter.in.rest.dto.mapper;

import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.PacienteRequestDTO;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.PacienteUpdateDTO;

public class PacienteRequestMapper {

    private PacienteRequestMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CreatePacienteCommand toCreateCommand(PacienteRequestDTO dto) {
        return CreatePacienteCommand.builder()
                .dni(dto.getDni())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .obraSocial(dto.getObraSocial())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .build();
    }


    public static UpdatePacienteCommand toUpdateCommand(PacienteUpdateDTO dto) {
        return UpdatePacienteCommand.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .obraSocial(dto.getObraSocial())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .build();
    }


}
