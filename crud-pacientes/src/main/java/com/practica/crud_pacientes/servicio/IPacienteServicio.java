package com.practica.crud_pacientes.servicio;

import com.practica.crud_pacientes.dto.PacienteDto;
import com.practica.crud_pacientes.excepciones.PacienteNoEncontradoException;

import java.util.List;

public interface IPacienteServicio {

    List<PacienteDto> getPacientes();
    PacienteDto getPacientePorId(Integer idPaciente) throws PacienteNoEncontradoException;
    PacienteDto getPacientePorDni(String dni);
    List<PacienteDto> getPacientePorNombre(String nombre);
    PacienteDto addPaciente(PacienteDto pacienteDto);
    PacienteDto updatePaciente(Integer idPaciente, PacienteDto pacienteDto) throws PacienteNoEncontradoException;
    void deletePaciente(Integer idPaciente) throws PacienteNoEncontradoException;

}
