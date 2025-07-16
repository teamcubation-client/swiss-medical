package com.practica.crud_pacientes.application.service;

import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.in.PacienteUseCase;
import com.practica.crud_pacientes.application.domain.port.out.PacienteRepositoryPort;
import com.practica.crud_pacientes.shared.exceptions.PacienteDuplicadoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteService implements PacienteUseCase {

    private final PacienteRepositoryPort pacienteRepositoryPort;

    public PacienteService(PacienteRepositoryPort pacienteRepositoryPort) {
        this.pacienteRepositoryPort = pacienteRepositoryPort;
    }

    @Override
    @Transactional
    public List<Paciente> getPacientes() {
        return pacienteRepositoryPort.findAll();
    }

    @Override
    @Transactional
    public Paciente getPacienteById(int id) throws PacienteNoEncontradoException {
        return pacienteRepositoryPort.findById(id);
    }

    @Override
    @Transactional
    public Paciente addPaciente(Paciente paciente) {
        if(pacienteRepositoryPort.getByDniFromSP(paciente.getDni()) == null)
            throw new PacienteDuplicadoException();

        return pacienteRepositoryPort.save(paciente);
    }

    @Override
    @Transactional
    public Paciente updatePaciente(int id, Paciente paciente) throws PacienteNoEncontradoException {
        pacienteRepositoryPort.findById(id);

        Paciente existingPaciente = pacienteRepositoryPort.getByDniFromSP(paciente.getDni());
        if (existingPaciente == null)
            throw new PacienteDuplicadoException();

        paciente.setId(id);
        return pacienteRepositoryPort.save(paciente);
    }

    @Override
    @Transactional
    public void deletePaciente(int id) throws PacienteNoEncontradoException {
        if(!pacienteRepositoryPort.existsById(id))
            throw new PacienteNoEncontradoException();

        pacienteRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional
    public Paciente getByDni(String dni) {
        Paciente paciente = pacienteRepositoryPort.getByDniFromSP(dni);
        if(paciente == null)
            throw new PacienteNoEncontradoException();

        return paciente;
    }

    @Override
    @Transactional
    public List<Paciente> getPacientesbyName(String nombre) {
        return pacienteRepositoryPort.getPacientesByNombreFromSP(nombre.toLowerCase());
    }

    @Override
    @Transactional
    public List<Paciente> getPacietesbyObraSocial(String obraSocial, int limite, int off) {
        return pacienteRepositoryPort.getPacietesbyObraSocialFromSP(obraSocial, limite, off);
    }
}
