package com.tq.pacientes.mappers;

import com.tq.pacientes.dtos.PacienteDTO;
import com.tq.pacientes.models.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {
    public
    PacienteDTO toDto(Paciente paciente) {
        return new PacienteDTO(
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getDni(),
                paciente.getObraSocial()
        );
    }

    public Paciente toEntity(PacienteDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNombre(dto.nombre());
        paciente.setApellido(dto.apellido());
        paciente.setDni(dto.dni());
        paciente.setObraSocial(dto.obraSocial());
        return paciente;
    }
}
