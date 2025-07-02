package swissmedical.mapper;

import org.springframework.stereotype.Component;
import swissmedical.model.Paciente;
import swissmedical.dto.PacienteDTO;

@Component
public class PacienteMapper {
    public PacienteDTO toDTO(Paciente paciente) {
        return new PacienteDTO(
            paciente.getId(),
            paciente.getNombre(),
            paciente.getApellido(),
            paciente.getDni(),
            paciente.getObraSocial(),
            paciente.getEmail(),
            paciente.getTelefono()
        );
    }

    public Paciente toEntity(PacienteDTO dto) {
        return new Paciente(
            dto.getId(),
            dto.getNombre(),
            dto.getApellido(),
            dto.getDni(),
            dto.getObraSocial(),
            dto.getEmail(),
            dto.getTelefono()
        );
    }
} 