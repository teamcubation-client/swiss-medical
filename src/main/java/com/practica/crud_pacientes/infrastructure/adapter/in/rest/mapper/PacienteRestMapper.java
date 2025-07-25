package com.practica.crud_pacientes.infrastructure.adapter.in.rest.mapper;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteRequest;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PacienteRestMapper {

    PacienteRestMapper mapper = Mappers.getMapper(PacienteRestMapper.class);

    Paciente requestToDomain(PacienteRequest pacienteRequest);
    PacienteRequest domainToRequest(Paciente paciente);

    Paciente responseToDomain(PacienteResponse pacienteResponse);
    PacienteResponse domainToResponse(Paciente paciente);
}
