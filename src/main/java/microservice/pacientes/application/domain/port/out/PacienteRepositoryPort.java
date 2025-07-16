package microservice.pacientes.application.domain.port.out;

import microservice.pacientes.application.domain.model.Paciente;

import java.util.List;
import java.util.Optional;

public interface PacienteRepositoryPort {
    List<Paciente> getAll();
    Optional<Paciente> getByDni(String dni);
    List<Paciente>  getByNombreContainingIgnoreCase(String nombre);
    Paciente save(Paciente paciente);
    boolean delete(Paciente paciente);
    Optional<Paciente> getByNombre(String nombre);
    List<Paciente> getByObraSocial(String obraSocial, int limit, int offset);
    boolean existsByDni(String dni);
}
