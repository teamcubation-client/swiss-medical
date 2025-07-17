package microservice.pacientes.infrastructure.adapter.out.persistence.mysql.mapper;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.entity.PacienteEntity;

import java.util.List;

public class PacienteEntityMapper {

    public static Paciente toDomain(PacienteEntity pacienteEntity) {
        return Paciente.builder()
                .dni(pacienteEntity.getDni())
                .nombre(pacienteEntity.getNombre())
                .apellido(pacienteEntity.getApellido())
                .obraSocial(pacienteEntity.getObraSocial())
                .email(pacienteEntity.getEmail())
                .telefono(pacienteEntity.getTelefono())
                .build();
    }


    public static List<Paciente> toDomain(List<PacienteEntity> pacientesEntity) {
        return pacientesEntity.stream().map(PacienteEntityMapper::toDomain).toList(); // method reference
    }

    public static PacienteEntity toEntity(Paciente paciente) {
        return PacienteEntity.builder()
                .dni(paciente.getDni())
                .nombre(paciente.getNombre())
                .apellido(paciente.getApellido())
                .obraSocial(paciente.getObraSocial())
                .email(paciente.getEmail())
                .telefono(paciente.getTelefono())
                .build();
    }

}
