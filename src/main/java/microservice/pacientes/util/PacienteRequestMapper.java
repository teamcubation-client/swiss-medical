package microservice.pacientes.util;

import microservice.pacientes.controller.dto.PacienteRequestDTO;
import microservice.pacientes.model.Paciente;

import java.util.List;

public class PacienteRequestMapper {

    public static List<PacienteRequestDTO> toDTO(List<Paciente> pacientes) {
        return pacientes
                .stream()
                .map(PacienteRequestMapper::toDTO)
                .toList();
    }

    public static PacienteRequestDTO toDTO(Paciente paciente) {
        return new PacienteRequestDTO(
                paciente.getDni(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getObra_social(),
                paciente.getEmail(),
                paciente.getTelefono()
        );
    }

    public static Paciente toEntity(PacienteRequestDTO dto) {
        return new Paciente(
                dto.getDni(),
                dto.getNombre(),
                dto.getApellido(),
                dto.getObraSocial(),
                dto.getEmail(),
                dto.getTelefono()
        );
    }

}
