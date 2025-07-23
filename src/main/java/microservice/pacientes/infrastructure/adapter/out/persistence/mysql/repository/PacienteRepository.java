package microservice.pacientes.infrastructure.adapter.out.persistence.mysql.repository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOut;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.entity.PacienteEntity;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.mapper.PacienteEntityMapper;
import microservice.pacientes.shared.annotations.PersistenceAdapter;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@AllArgsConstructor
public class PacienteRepository implements PacientePortOut {

    private final JpaPacienteRepository jpaPacienteRepository;
    private final PacienteEntityMapper pacienteEntityMapper;
    @Override
    public List<Paciente> getAll() {
        List<PacienteEntity> pacientesEntity = jpaPacienteRepository.findAll();
        List<Paciente> pacientes = pacienteEntityMapper.toDomain(pacientesEntity);
        return pacientes;
    }

    @Override
    public Optional<Paciente> getByDni(String dni) {
        Optional<PacienteEntity> pacienteEntity = jpaPacienteRepository.findByDni(dni);
        Optional<Paciente> paciente = pacienteEntity.map(pacienteEntityMapper::toDomain);
        return paciente;
    }

    @Override
    public List<Paciente> getByNombreContainingIgnoreCase(String nombre) {
        List<PacienteEntity> pacientesEntity = jpaPacienteRepository.findByNombreContainingIgnoreCase(nombre);
        List<Paciente> pacientes = pacienteEntityMapper.toDomain(pacientesEntity);
        return pacientes;
    }

    @Override
    public Paciente save(Paciente paciente) {
        jpaPacienteRepository.save(pacienteEntityMapper.toEntity(paciente));
        return paciente;
    }

    @Override
    public void delete(Paciente paciente) {
        PacienteEntity pacienteEntity = pacienteEntityMapper.toEntity(paciente);
        jpaPacienteRepository.delete(pacienteEntity);
    }

    @Override
    @Transactional
    public Optional<Paciente> getByNombre(String nombre) {
        Optional<PacienteEntity> pacienteEntity = jpaPacienteRepository.findByNombreSP(nombre);
        Optional<Paciente> paciente = pacienteEntity.map(pacienteEntityMapper::toDomain);
        return paciente;
    }

    @Override
    @Transactional
    public List<Paciente> getByObraSocial(String obraSocial, int limit, int offset) {
        List<PacienteEntity> pacientesEntity = jpaPacienteRepository.findByObraSocialSP(obraSocial, limit, offset);
        List<Paciente> pacientes = pacienteEntityMapper.toDomain(pacientesEntity);
        return pacientes;
    }

    @Override
    public boolean existsByDni(String dni) {
        boolean exists = jpaPacienteRepository.existsByDni(dni);
        return exists;
    }
}
