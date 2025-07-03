package com.tq.pacientes.services;

import com.tq.pacientes.dtos.ActualizarPacienteDTO;
import com.tq.pacientes.dtos.PacienteDTO;
import com.tq.pacientes.models.Paciente;

import java.util.List;

public interface PacienteService {

    PacienteDTO crearPaciente(Paciente paciente);
    List<PacienteDTO> listarPacientes();
    PacienteDTO buscarPorId(Long id);
    PacienteDTO buscarPorDni(String dni);
    List<PacienteDTO> buscarPorNombre(String nombre);
    PacienteDTO actualizar(Long id, ActualizarPacienteDTO dto);
    void eliminar(Long id);
}
