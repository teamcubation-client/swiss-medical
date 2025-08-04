package com.practica.crud_pacientes.infrastructure.adapter.out.persistence;

import com.practica.crud_pacientes.application.domain.port.out.PacienteLoggerPort;
import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.out.PacienteRepositoryPort;
import com.practica.crud_pacientes.infrastructure.adapter.out.entity.PacienteEntity;
import com.practica.crud_pacientes.infrastructure.adapter.out.mapper.PacientePersistanceMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class PacienteRepositoryAdapter implements PacienteRepositoryPort {

    private final PacienteJpaRepository pacienteJpaRepository;
    private final PacientePersistanceMapper mapper;
    private final PacienteLoggerPort loggerPort;

    @Override
    public Paciente save(Paciente paciente) {
        loggerPort.info("Guardando paciente con DNI: {}", paciente.getDni());
        PacienteEntity pacienteNuevo = mapper.domainToEntity(paciente);
        PacienteEntity pacienteGuardado = pacienteJpaRepository.save(pacienteNuevo);
        return mapper.entityToDomain(pacienteGuardado);
    }

    @Override
    public List<Paciente> findAll() {
        loggerPort.debug("Recuperando todos los pacientes desde la base de datos");
        List<PacienteEntity> pacientesEntity = pacienteJpaRepository.findAll();
        return pacientesEntity.stream()
                .map(mapper::entityToDomain)
                .toList();
    }

    @Override
    public Paciente findById(int id) {
        loggerPort.info("Buscando paciente por ID: {}", id);
        PacienteEntity pacienteEntity = pacienteJpaRepository.findById(id).orElseThrow(() -> {
            loggerPort.warn("No se encontró paciente con ID: {}", id);
            return new PacienteNoEncontradoException();
        });
        return mapper.entityToDomain(pacienteEntity);
    }

    @Override
    public Boolean existsById(int id) {
        loggerPort.debug("Verificando existencia de paciente");
        return pacienteJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(int id) {
        loggerPort.info("Eliminando paciente con ID: {}", id);
        pacienteJpaRepository.deleteById(id);
    }

    @Override
    public Paciente getPacienteByDni(String dni) {
        loggerPort.info("Buscando paciente por DNI: {}", dni);
        PacienteEntity pacienteEntity = pacienteJpaRepository.getByDni(dni);
        return mapper.entityToDomain(pacienteEntity);
    }

    @Override
    public List<Paciente> getPacientesByNombre(String nombre) {
        loggerPort.info("Buscando pacientes por nombre: {}", nombre);
        List<PacienteEntity> pacientesEntity = pacienteJpaRepository.getPacientesByNombre(nombre.toLowerCase());
        return pacientesEntity.stream()
                .map(mapper::entityToDomain)
                .toList();
    }

    @Override
    public List<Paciente> getPacientesByObraSocial(String obraSocial, int limite, int off) {
        loggerPort.info("Buscando pacientes por obra social: {} (limite={}, offset={})", obraSocial, limite, off);
        List<PacienteEntity> pacientesEntity = pacienteJpaRepository.getPacietesbyObraSocial(obraSocial, limite, off);
        return pacientesEntity.stream()
                .map(mapper::entityToDomain)
                .toList();
    }
}
