package com.practica.crud_pacientes.application.service;

import com.practica.crud_pacientes.application.domain.port.out.PacienteEventPublisher;
import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.in.PacienteUseCase;
import com.practica.crud_pacientes.application.domain.port.out.PacienteRepositoryPort;
import com.practica.crud_pacientes.shared.exceptions.PacienteDuplicadoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PacienteService implements PacienteUseCase {

    private final PacienteRepositoryPort pacienteRepositoryPort;
    private final PacienteEventPublisher eventPublisher;

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> getPacientes() {
        return pacienteRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Paciente getPacienteById(int id) throws PacienteNoEncontradoException {
        return pacienteRepositoryPort.findById(id);
    }

    @Override
    @Transactional
    public Paciente addPaciente(Paciente paciente) {
        if(pacienteRepositoryPort.getPacienteByDni(paciente.getDni()) != null)
            throw new PacienteDuplicadoException();

        Paciente pacienteCreado = pacienteRepositoryPort.save(paciente);
        eventPublisher.publishPacienteCreado(pacienteCreado);

        return pacienteCreado;
    }

    @Override
    @Transactional
    public Paciente updatePaciente(int id, Paciente paciente) throws PacienteNoEncontradoException {
        Optional.ofNullable(pacienteRepositoryPort.findById(id))
                .orElseThrow(PacienteNoEncontradoException::new);

        Paciente existingPaciente = pacienteRepositoryPort.getPacienteByDni(paciente.getDni());
        if (existingPaciente != null && existingPaciente.getId() != id)
            throw new PacienteDuplicadoException();

        paciente.setId(id);
        return pacienteRepositoryPort.save(paciente);
    }

    @Override
    @Transactional
    public void deletePaciente(int id) throws PacienteNoEncontradoException {
        Paciente pacienteAEliminar = Optional.ofNullable(pacienteRepositoryPort.findById(id))
                .orElseThrow(PacienteNoEncontradoException::new);

        pacienteRepositoryPort.deleteById(id);
        eventPublisher.publishPacienteEliminado(pacienteAEliminar);
    }

    @Override
    @Transactional
    public Paciente getPacienteByDni(String dni) {
        Paciente paciente = pacienteRepositoryPort.getPacienteByDni(dni);
        if(paciente == null)
            throw new PacienteNoEncontradoException();

        return paciente;
    }

    @Override
    @Transactional
    public List<Paciente> getPacientesByName(String nombre) {
        return pacienteRepositoryPort.getPacientesByNombre(nombre.toLowerCase());
    }

    @Override
    @Transactional
    public List<Paciente> getPacientesByObraSocial(String obraSocial, int limite, int off) {
        return pacienteRepositoryPort.getPacientesByObraSocial(obraSocial, limite, off);
    }
}
