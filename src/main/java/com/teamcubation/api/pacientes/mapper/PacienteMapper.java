package com.teamcubation.api.pacientes.mapper;

import com.teamcubation.api.pacientes.dto.PacienteRequest;
import com.teamcubation.api.pacientes.dto.PacienteResponse;
import com.teamcubation.api.pacientes.model.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {
    public static Paciente toEntity(PacienteRequest request) {
        Paciente paciente = new Paciente();
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setDni(request.getDni());
        paciente.setObraSocial(request.getObraSocial());
        paciente.setEmail(request.getEmail());
        paciente.setTelefono(request.getTelefono());
        return paciente;
    }

    public static PacienteResponse toResponse(Paciente p) {
        PacienteResponse response = new PacienteResponse();
        response.setId(p.getId());
        response.setNombre(p.getNombre());
        response.setApellido(p.getApellido());
        response.setDni(p.getDni());
        return response;
    }

}
