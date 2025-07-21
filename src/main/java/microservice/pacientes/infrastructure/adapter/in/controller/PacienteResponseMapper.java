package microservice.pacientes.infrastructure.adapter.in.controller;

import microservice.pacientes.shared.PacienteNotFoundException;
import microservice.pacientes.application.domain.model.Paciente;

/**
 * Mapper para convertir entre entidades Paciente y sus DTO
 * Facilita la transformacion de datos entre las capas de persistencia y presentacion
 */
public final class PacienteResponseMapper {


    //impide una instanciacion
    private PacienteResponseMapper() {}

    /**
     * Convierte un Paciente a PacienteDTO
     */
    public static PacienteDTO toDTO(Paciente paciente) {
        if (paciente == null){
            throw PacienteNotFoundException.porId(null);
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

    /**
     * Convierte un Paciente DTO a Paciente
     */
    public static Paciente toModel(PacienteDTO pacienteDTO) {
        return Paciente.builder()
                .id(pacienteDTO.getId())
                .nombre(pacienteDTO.getNombre())
                .apellido(pacienteDTO.getApellido())
                .dni(pacienteDTO.getDni())
                .obraSocial(pacienteDTO.getObraSocial())
                .email(pacienteDTO.getEmail())
                .telefono(pacienteDTO.getTelefono())
                .tipoPlanObraSocial(pacienteDTO.getTipoPlanObraSocial())
                .fechaAlta(pacienteDTO.getFechaAlta())
                .estado(pacienteDTO.isEstado())
                .build();
    }

} 