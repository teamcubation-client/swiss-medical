package microservice.pacientes.infrastructure.adapter.out.persistence;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.infrastructure.adapter.in.controller.PacienteDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre entidades Paciente y sus DTO
 * Facilita la transformacion de datos entre las capas de persistencia y presentacion
 */
@Component
public class PacienteMapper {
    /**
     * Convierte un Paciente a una Entidad Paciente
     */
    public PacienteEntity toEntity(Paciente paciente) {
        PacienteEntity pacienteEntity = new PacienteEntity();
        pacienteEntity.setId(paciente.getId());
        pacienteEntity.setNombre(paciente.getNombre());
        pacienteEntity.setApellido(paciente.getApellido());
        pacienteEntity.setDni(paciente.getDni());
        pacienteEntity.setObraSocial(paciente.getObraSocial());
        pacienteEntity.setEmail(paciente.getEmail());
        pacienteEntity.setTelefono(paciente.getTelefono());
        pacienteEntity.setTipoPlanObraSocial(paciente.getTipoPlanObraSocial());
        pacienteEntity.setFechaAlta(paciente.getFechaAlta());
        pacienteEntity.setEstado(paciente.isEstado());
        return pacienteEntity;
    }

    /**
     * Convierte una Entidad Paciente a Paciente
     */
    public Paciente toModel(PacienteEntity pacienteEntity) {
        Paciente paciente = new Paciente();
        paciente.setId(pacienteEntity.getId());
        paciente.setNombre(pacienteEntity.getNombre());
        paciente.setApellido(pacienteEntity.getApellido());
        paciente.setDni(pacienteEntity.getDni());
        paciente.setObraSocial(pacienteEntity.getObraSocial());
        paciente.setEmail(pacienteEntity.getEmail());
        paciente.setTelefono(pacienteEntity.getTelefono());
        paciente.setTipoPlanObraSocial(pacienteEntity.getTipoPlanObraSocial());
        paciente.setFechaAlta(pacienteEntity.getFechaAlta());
        paciente.setEstado(pacienteEntity.isEstado());
        return paciente;
    }
} 