package microservice.pacientes.infrastructure.adapter.in.rest.dto.mapper;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.infrastructure.adapter.in.rest.dto.PacienteResponseDTO;

import java.util.List;

public class PacienteResponseMapper {

        public static List<PacienteResponseDTO> toDTO(List<Paciente> pacientes) {
            return pacientes
                    .stream()
                    .map(PacienteResponseMapper::toDTO)
                    .toList();
        }

        public static PacienteResponseDTO toDTO(Paciente paciente) {
            return PacienteResponseDTO.builder()
                    .dni(paciente.getDni())
                    .nombre(paciente.getNombre())
                    .apellido(paciente.getApellido())
                    .obraSocial(paciente.getObraSocial())
                    .build();
        }

}
