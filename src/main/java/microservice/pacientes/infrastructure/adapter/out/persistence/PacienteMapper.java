package microservice.pacientes.infrastructure.adapter.out.persistence;

import microservice.pacientes.application.domain.model.Paciente;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre entidades Paciente y sus DTO
 * Facilita la transformacion de datos entre las capas de persistencia y presentacion
 */
public final class PacienteMapper {

    //impide una instanciacion
    private PacienteMapper() {}
    /**
     * Convierte un Paciente a una Entidad Paciente
     */
    public static PacienteEntity toEntity(Paciente paciente) {
        return PacienteEntity.builder()
                .id(paciente.getId())
                .nombre(paciente.getNombre())
                .apellido(paciente.getApellido())
                .dni(paciente.getDni())
                .obraSocial(paciente.getObraSocial())
                .email(paciente.getEmail())
                .telefono(paciente.getTelefono())
                .tipoPlanObraSocial(paciente.getTipoPlanObraSocial())
                .fechaAlta(paciente.getFechaAlta())
                .estado(paciente.isEstado())
                .build();
    }

    /**
     * Convierte una Entidad Paciente a Paciente
     */
    public static Paciente toModel(PacienteEntity pacienteEntity) {
        return Paciente.builder()
                .id(pacienteEntity.getId())
                .nombre(pacienteEntity.getNombre())
                .apellido(pacienteEntity.getApellido())
                .dni(pacienteEntity.getDni())
                .obraSocial(pacienteEntity.getObraSocial())
                .email(pacienteEntity.getEmail())
                .telefono(pacienteEntity.getTelefono())
                .tipoPlanObraSocial(pacienteEntity.getTipoPlanObraSocial())
                .fechaAlta(pacienteEntity.getFechaAlta())
                .estado(pacienteEntity.isEstado())
                .build();
    }
} 