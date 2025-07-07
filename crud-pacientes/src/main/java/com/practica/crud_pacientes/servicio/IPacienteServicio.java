package com.practica.crud_pacientes.servicio;

import com.practica.crud_pacientes.excepciones.PacienteNoEncontradoException;
import com.practica.crud_pacientes.modelo.Paciente;

import java.util.List;

public interface IPacienteServicio {

    List<Paciente> getPacientes();

    Paciente getPacientePorId(int id) throws PacienteNoEncontradoException;

    Paciente getPacientePorDni(String dni);

    List<Paciente> getPacientePorNombre(String nombre);

    Paciente addPaciente(Paciente pacienteDto);

    Paciente updatePaciente(int id, Paciente pacienteDto) throws PacienteNoEncontradoException;

    void deletePaciente(int id) throws PacienteNoEncontradoException;

}
