package com.practica.crud_pacientes.service;

import com.practica.crud_pacientes.exceptions.PacienteDuplicadoException;
import com.practica.crud_pacientes.exceptions.PacienteNoEncontradoException;
import com.practica.crud_pacientes.model.Paciente;
import com.practica.crud_pacientes.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteServiceImpl implements IPacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public List<Paciente> getPacientes() {
        return pacienteRepository.findAll();
    }

    @Override
    public Paciente getPacienteById(int id) throws PacienteNoEncontradoException {
        return pacienteRepository.findById(id).orElseThrow(PacienteNoEncontradoException::new);
    }

    @Override
    public Paciente getPacienteByDni(String dni) {
        Paciente paciente = pacienteRepository.findByDni(dni);

        if (paciente == null) {
            throw new PacienteNoEncontradoException();
        }
        return paciente;
    }

    @Override
    public List<Paciente> getPacienteByName(String nombre) {
        return pacienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Paciente addPaciente(Paciente paciente) {
        if (pacienteRepository.findByDni(paciente.getDni()) != null) {
            throw new PacienteDuplicadoException();
        }
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente updatePaciente(int id, Paciente paciente) throws PacienteNoEncontradoException {

        pacienteRepository.findById(id)
                .orElseThrow(PacienteNoEncontradoException::new);

        Paciente existingPaciente = pacienteRepository.findByDni(paciente.getDni());
        if (existingPaciente != null)
            throw new PacienteDuplicadoException();

        paciente.setId(id);
        return pacienteRepository.save(paciente);
    }

    @Override
    public void deletePaciente(int id) throws PacienteNoEncontradoException {
        if (!pacienteRepository.existsById(id))
            throw new PacienteNoEncontradoException();

        pacienteRepository.deleteById(id);
    }
}
