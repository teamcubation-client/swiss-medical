package com.teamcubation.api.pacientes.service;

import com.teamcubation.api.pacientes.model.Paciente;

import java.util.List;

public interface IPacienteService {
    Paciente crear(Paciente request);
    List<Paciente> obtenerTodos(String dni, String nombre);
    Paciente obtenerPorID(long id);
    Paciente actualizarPorID(long id, Paciente paciente);
    void borrarPorID(long id);
}
