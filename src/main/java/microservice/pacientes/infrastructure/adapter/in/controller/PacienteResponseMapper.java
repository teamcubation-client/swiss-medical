package microservice.pacientes.infrastructure.adapter.in.controller;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.PacienteNullException;

/**
 * Mapper para convertir entre entidades Paciente y sus DTO
 * Facilita la transformacion de datos entre las capas de persistencia y presentacion
 */
public final class PacienteResponseMapper {

    private PacienteResponseMapper() {}

    public static PacienteDTO toDTO(Paciente paciente) {
        if (paciente == null){
            throw new PacienteNullException();
        };
        return PacienteDTO.builder()
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

    public static Paciente toModel(PacienteDTO pacienteDTO) {
        if (pacienteDTO == null) {
            throw new PacienteNullException();
        }
        return Paciente.builder()
                .nombre(pacienteDTO.getNombre())
                .apellido(pacienteDTO.getApellido())
                .dni(pacienteDTO.getDni())
                .obraSocial(pacienteDTO.getObraSocial())
                .email(pacienteDTO.getEmail())
                .telefono(pacienteDTO.getTelefono())
                .tipoPlanObraSocial(pacienteDTO.getTipoPlanObraSocial())
                .fechaAlta(pacienteDTO.getFechaAlta())
                .estado(pacienteDTO.getEstado())
                .build();
    }

} 