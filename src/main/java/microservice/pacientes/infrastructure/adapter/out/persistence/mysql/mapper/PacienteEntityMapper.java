package microservice.pacientes.infrastructure.adapter.out.persistence.mysql.mapper;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.entity.PacienteEntity;

import java.util.List;

public class PacienteEntityMapper {

    public static Paciente toDomain(PacienteEntity pacienteEntity) {
        return new Paciente(
                pacienteEntity.getDni(),
                pacienteEntity.getNombre(),
                pacienteEntity.getApellido(),
                pacienteEntity.getObra_social(),
                pacienteEntity.getEmail(),
                pacienteEntity.getTelefono()
        );
    }

    public static List<Paciente> toDomain(List<PacienteEntity> pacientesEntity) {
        return pacientesEntity.stream().map(PacienteEntityMapper::toDomain).toList();
    }

    public static PacienteEntity toEntity(Paciente paciente) {
        return new PacienteEntity(
                paciente.getDni(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getObra_social(),
                paciente.getEmail(),
                paciente.getTelefono()
        );
    }

}
