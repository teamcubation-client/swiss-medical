package swissmedical.mapper;

import org.springframework.stereotype.Component;
import swissmedical.model.Paciente;
import swissmedical.dto.PacienteDTO;

/**
 * Mapper para convertir entre entidades Paciente y sus DTO
 * Facilita la transformacion de datos entre las capas de persistencia y presentacion
 */
@Component
public class PacienteMapper {
    /**
     * Convierte una entidad Paciente a PacienteDTO
     * @param paciente entidad Paciente a convertir
     * @return PacienteDTO con los datos del paciente
     */
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

    /**
     * Convierte un PacienteDTO a entidad Paciente
     * @param dto DTO a convertir
     * @return entidad Paciente con los datos del DTO
     */
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