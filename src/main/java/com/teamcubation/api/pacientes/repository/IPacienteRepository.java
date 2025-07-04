package com.teamcubation.api.pacientes.repository;

import com.teamcubation.api.pacientes.model.Paciente;

import java.util.List;
import java.util.Optional;

public interface IPacienteRepository {
    Paciente guardar(Paciente paciente);
    Optional<Paciente> buscarPorId(Long id);
    List<Paciente> buscarTodos(String dni, String nombre);
    boolean actualizar(Paciente paciente);
    boolean borrar(Long id);
}
