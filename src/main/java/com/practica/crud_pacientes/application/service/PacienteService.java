package com.practica.crud_pacientes.application.service;

import com.practica.crud_pacientes.application.domain.port.out.PacienteEventPublisher;
import com.practica.crud_pacientes.application.domain.port.out.PacienteLoggerPort;
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
    private final PacienteLoggerPort loggerPort;

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> getPacientes() {
        loggerPort.info("Obteniendo todos los pacientes");
        return pacienteRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Paciente getPacienteById(int id) throws PacienteNoEncontradoException {
        loggerPort.info("Buscando paciente por ID: {}", id);
        return pacienteRepositoryPort.findById(id);
    }

    @Override
    @Transactional
    public Paciente addPaciente(Paciente paciente) {
        loggerPort.info("Intentando crear paciente con DNI: {}", paciente.getDni());
        if(pacienteRepositoryPort.getPacienteByDni(paciente.getDni()) != null) {
            loggerPort.warn("Paciente duplicado detectado con DNI: {}", paciente.getDni());
            throw new PacienteDuplicadoException();
        }

        Paciente pacienteCreado = pacienteRepositoryPort.save(paciente);
        eventPublisher.publishPacienteCreado(pacienteCreado);

        return pacienteCreado;
    }

    @Override
    @Transactional
    public Paciente updatePaciente(int id, Paciente paciente) throws PacienteNoEncontradoException {
        loggerPort.info("Actualizando paciente con ID: {}", id);
        Optional.ofNullable(pacienteRepositoryPort.findById(id))
                .orElseThrow(() -> {
                    loggerPort.warn("No se encontró paciente con ID: {}", id);
                    return new PacienteNoEncontradoException();
                });

        Paciente existingPaciente = pacienteRepositoryPort.getPacienteByDni(paciente.getDni());
        if (existingPaciente != null && existingPaciente.getId() != id) {
            loggerPort.warn("Conflicto de DNI al actualizar paciente. DNI: {} ya pertenece a otro paciente con ID: {}", paciente.getDni(), existingPaciente.getId());
            throw new PacienteDuplicadoException();
        }

        paciente.setId(id);
        loggerPort.info("Paciente actualizado correctamente");
        return pacienteRepositoryPort.save(paciente);
    }

    @Override
    @Transactional
    public void deletePaciente(int id) throws PacienteNoEncontradoException {
        loggerPort.info("Eliminando paciente con ID: {}", id);
        Paciente pacienteAEliminar = Optional.ofNullable(pacienteRepositoryPort.findById(id))
                .orElseThrow(() -> {
                    loggerPort.warn("No se encontró paciente con ID: {} para eliminar", id);
                    return new PacienteNoEncontradoException();
                });

        pacienteRepositoryPort.deleteById(id);
        eventPublisher.publishPacienteEliminado(pacienteAEliminar);
    }

    @Override
    @Transactional
    public Paciente getPacienteByDni(String dni) {
        loggerPort.info("Buscando paciente por DNI: {}", dni);
        Paciente paciente = pacienteRepositoryPort.getPacienteByDni(dni);
        if(paciente == null) {
            loggerPort.warn("No se encontró paciente con DNI: {}", dni);
            throw new PacienteNoEncontradoException();
        }
        return paciente;
    }

    @Override
    @Transactional
    public List<Paciente> getPacientesByName(String nombre) {
        loggerPort.info("Buscando pacientes por nombre: {}", nombre);
        return pacienteRepositoryPort.getPacientesByNombre(nombre.toLowerCase());
    }

    @Override
    @Transactional
    public List<Paciente> getPacientesByObraSocial(String obraSocial, int limite, int off) {
        loggerPort.info("Buscando pacientes por obra social: {} (limite={}, offset={})", obraSocial, limite, off);
        return pacienteRepositoryPort.getPacientesByObraSocial(obraSocial, limite, off);
    }
}
