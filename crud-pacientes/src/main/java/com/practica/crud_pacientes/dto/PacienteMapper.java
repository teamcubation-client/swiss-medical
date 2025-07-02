package com.practica.crud_pacientes.dto;

import com.practica.crud_pacientes.modelo.Paciente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PacienteMapper {

    PacienteMapper mapper = Mappers.getMapper(PacienteMapper.class);

    //@Mapping(source = "imagePath", target = "imageUrl")
    PacienteDto pacienteToPacienteDto(Paciente paciente);

    //@Mapping(source = "imageUrl", target = "imagePath")
    Paciente pacienteDtoToPaciente(PacienteDto pacientedto);
}
