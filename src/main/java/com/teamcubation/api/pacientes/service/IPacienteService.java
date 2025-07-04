package com.teamcubation.api.pacientes.service;

import com.teamcubation.api.pacientes.dto.PacienteRequest;
import com.teamcubation.api.pacientes.dto.PacienteResponse;

import java.util.List;

public interface IPacienteService {
    PacienteResponse crearPaciente(PacienteRequest request);
    List<PacienteResponse> obtenerPacientes(String dni, String nombre);
    PacienteResponse obtenerPacientePorId(Long id);
    PacienteResponse actualizarPaciente(Long id, PacienteRequest request);
    void borrarPaciente(Long id);
}
