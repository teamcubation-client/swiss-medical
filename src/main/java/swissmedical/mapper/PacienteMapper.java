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
        if (paciente == null) return null;
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setNombre(paciente.getNombre());
        dto.setApellido(paciente.getApellido());
        dto.setDni(paciente.getDni());
        dto.setObraSocial(paciente.getObraSocial());
        dto.setEmail(paciente.getEmail());
        dto.setTelefono(paciente.getTelefono());
        dto.setTipoPlanObraSocial(paciente.getTipoPlanObraSocial());
        dto.setFechaAlta(paciente.getFechaAlta());
        dto.setEstado(paciente.isEstado());
        return dto;
    }

    /**
     * Convierte un PacienteDTO a entidad Paciente
     * @param dto DTO a convertir
     * @return entidad Paciente con los datos del DTO
     */
    public Paciente toEntity(PacienteDTO dto) {
        if (dto == null) return null;
        Paciente paciente = new Paciente();
        paciente.setId(dto.getId());
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setDni(dto.getDni());
        paciente.setObraSocial(dto.getObraSocial());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefono(dto.getTelefono());
        paciente.setTipoPlanObraSocial(dto.getTipoPlanObraSocial());
        paciente.setFechaAlta(dto.getFechaAlta());
        paciente.setEstado(dto.isEstado());
        return paciente;
    }
} 