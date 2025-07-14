package microservice.pacientes.util;

import microservice.pacientes.controller.dto.PacienteResponseDTO;
import microservice.pacientes.model.Paciente;

import java.util.List;

public class PacienteResponseMapper {

        public static List<PacienteResponseDTO> toDTO(List<Paciente> pacientes) {
            return pacientes
                    .stream()
                    .map(PacienteResponseMapper::toDTO)
                    .toList();
        }

        public static PacienteResponseDTO toDTO(Paciente paciente) {
            return new PacienteResponseDTO(
                    paciente.getDni(),
                    paciente.getNombre(),
                    paciente.getApellido(),
                    paciente.getObra_social()
            );
        }

        public static Paciente toEntity(PacienteResponseDTO dto) {
            return new Paciente(
                    dto.getDni(),
                    dto.getNombre(),
                    dto.getApellido(),
                    dto.getObraSocial(),
                    "",
                    ""
            );
        }

}
