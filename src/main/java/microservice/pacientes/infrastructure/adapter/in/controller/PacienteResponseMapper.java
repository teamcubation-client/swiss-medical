package microservice.pacientes.infrastructure.adapter.in.controller;

import microservice.pacientes.infrastructure.adapter.out.persistence.PacienteEntity;
import org.springframework.stereotype.Component;
import microservice.pacientes.application.domain.model.Paciente;

/**
 * Mapper para convertir entre entidades Paciente y sus DTO
 * Facilita la transformacion de datos entre las capas de persistencia y presentacion
 */
@Component
public class PacienteResponseMapper {
    /**
     * Convierte un Paciente a PacienteDTO
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
     * Convierte un Paciente DTO a Paciente
     */
    public Paciente toModel(PacienteDTO pacienteDTO) {
        Paciente paciente = new Paciente();
        paciente.setId(pacienteDTO.getId());
        paciente.setNombre(pacienteDTO.getNombre());
        paciente.setApellido(pacienteDTO.getApellido());
        paciente.setDni(pacienteDTO.getDni());
        paciente.setObraSocial(pacienteDTO.getObraSocial());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setTelefono(pacienteDTO.getTelefono());
        paciente.setTipoPlanObraSocial(pacienteDTO.getTipoPlanObraSocial());
        paciente.setFechaAlta(pacienteDTO.getFechaAlta());
        paciente.setEstado(pacienteDTO.isEstado());
        return paciente;
    }

} 