package swissmedical.service;

import swissmedical.model.Paciente;
import java.util.List;

public interface PacienteService {
    Paciente crearPaciente(Paciente paciente);
    Paciente obtenerPacientePorId(Long id);
    List<Paciente> listarPacientes();
    void eliminarPaciente(Long id);
    Paciente buscarPorDni(String dni);
    List<Paciente> buscarPorNombreParcial(String nombre);
    Paciente actualizarPaciente(Long id, Paciente paciente);
} 