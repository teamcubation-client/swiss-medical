package microservice.pacientes.infrastructure.adapter.out.persistence.mysql.repository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOut;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.mapper.PacienteEntityMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PacienteRepository implements PacientePortOut {

    private final JpaPacienteRepository jpaPacienteRepository;

    @Override
    public List<Paciente> getAll() {
        return PacienteEntityMapper.toDomain(jpaPacienteRepository.findAll());
    }

    @Override
    public Optional<Paciente> getByDni(String dni) {
        return jpaPacienteRepository.findByDniSP(dni).map(PacienteEntityMapper::toDomain);
    }

    @Override
    public List<Paciente> getByNombreContainingIgnoreCase(String nombre) {
        return PacienteEntityMapper.toDomain(jpaPacienteRepository.findByNombreContainingIgnoreCase(nombre));
    }

    @Override
    public Paciente save(Paciente paciente) {
        jpaPacienteRepository.save(PacienteEntityMapper.toEntity(paciente));
        return paciente;
    }

    @Override
    public void delete(Paciente paciente) {
        jpaPacienteRepository.delete(PacienteEntityMapper.toEntity(paciente));
    }

    @Override
    @Transactional
    public Optional<Paciente> getByNombre(String nombre) {
        return jpaPacienteRepository.findByNombreSP(nombre).map(PacienteEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public List<Paciente> getByObraSocial(String obraSocial, int limit, int offset) {
        return PacienteEntityMapper.toDomain(jpaPacienteRepository.findByObraSocialSP(obraSocial, limit, offset));
    }

    @Override
    public boolean existsByDni(String dni) {
        return jpaPacienteRepository.existsByDni(dni);
    }
}
