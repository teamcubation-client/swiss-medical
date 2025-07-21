package microservice.pacientes.infrastructure.adapter.out.persistence.mysql.mapper;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.factory.PacienteFactory;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.entity.PacienteEntity;
import microservice.pacientes.shared.annotations.Mapper;

import java.util.List;

@AllArgsConstructor
@Mapper
public class PacienteEntityMapper {

    private final PacienteFactory pacienteFactory;

    public Paciente toDomain(PacienteEntity pacienteEntity) {
        return pacienteFactory.create(
                pacienteEntity.getDni(),
                pacienteEntity.getNombre(),
                pacienteEntity.getApellido(),
                pacienteEntity.getObraSocial(),
                pacienteEntity.getEmail(),
                pacienteEntity.getTelefono()
        );
    }


    public List<Paciente> toDomain(List<PacienteEntity> pacientesEntity) {
        return pacientesEntity.stream().map(this::toDomain).toList(); // method reference
    }

    public PacienteEntity toEntity(Paciente paciente) {
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
