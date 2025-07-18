package com.practica.crud_pacientes.infrastructure.adapter.out.persistence;

import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.out.PacienteRepositoryPort;
import com.practica.crud_pacientes.infrastructure.adapter.out.entity.PacienteEntity;
import com.practica.crud_pacientes.infrastructure.adapter.out.mapper.PacientePersistanceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PacienteRepositoryAdapter implements PacienteRepositoryPort {

    private final PacienteJpaRepository pacienteJpaRepository;

    public PacienteRepositoryAdapter(PacienteJpaRepository pacienteJpaRepository){
        this.pacienteJpaRepository = pacienteJpaRepository;
    }

    @Override
    public Paciente save(Paciente paciente) {
        PacienteEntity pacienteNuevo = PacientePersistanceMapper.mapper.domainToEntity(paciente);
        PacienteEntity pacienteGuardado = pacienteJpaRepository.save(pacienteNuevo);
        return PacientePersistanceMapper.mapper.entityToDomain(pacienteGuardado);
    }

    @Override
    public List<Paciente> findAll() {
        List<PacienteEntity> pacientesEntity = pacienteJpaRepository.findAll();
        return pacientesEntity.stream()
                .map(PacientePersistanceMapper.mapper::entityToDomain)
                .toList();
    }

    @Override
    public Paciente findById(int id) {
        PacienteEntity pacienteEntity = pacienteJpaRepository.findById(id).orElseThrow(PacienteNoEncontradoException::new);
        return PacientePersistanceMapper.mapper.entityToDomain(pacienteEntity);
    }

    @Override
    public Boolean existsById(int id) {
        return pacienteJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(int id) {
        pacienteJpaRepository.deleteById(id);
    }

    @Override
    public Paciente getByDni(String dni) {
        PacienteEntity pacienteEntity = pacienteJpaRepository.getByDni(dni);
        return PacientePersistanceMapper.mapper.entityToDomain(pacienteEntity);
    }

    @Override
    public List<Paciente> getPacientesByNombre(String nombre) {
        List<PacienteEntity> pacientesEntity = pacienteJpaRepository.getPacientesByNombre(nombre.toLowerCase());
        return pacientesEntity.stream()
                .map(PacientePersistanceMapper.mapper::entityToDomain)
                .toList();
    }

    @Override
    public List<Paciente> getPacietesbyObraSocial(String obraSocial, int limite, int off) {
        List<PacienteEntity> pacientesEntity = pacienteJpaRepository.getPacietesbyObraSocial(obraSocial, limite, off);
        return pacientesEntity.stream()
                .map(PacientePersistanceMapper.mapper::entityToDomain)
                .toList();
    }
}
