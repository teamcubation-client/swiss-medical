package com.practica.crud_pacientes.infrastructure.adapter.out.mapper;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.infrastructure.adapter.out.entity.PacienteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PacientePersistanceMapper {

    PacientePersistanceMapper mapper = Mappers.getMapper(PacientePersistanceMapper.class);

    Paciente entityToDomain(PacienteEntity pacienteEntity);

    PacienteEntity domainToEntity(Paciente paciente);
}
