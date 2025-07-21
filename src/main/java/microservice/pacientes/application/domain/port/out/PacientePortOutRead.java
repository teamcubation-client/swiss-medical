package microservice.pacientes.application.domain.port.out;

import microservice.pacientes.application.domain.model.Paciente;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacientePortOutRead {

    Optional<Paciente> findByDni(String dni);

    List<Paciente> findByNombreContainingIgnoreCase(String nombre);

    Optional<Paciente> findById(Long id);

    List<Paciente> findAll();

    Optional<Paciente> buscarPorDniConSP(@Param("dni") String dni);

    List<Paciente> buscarPorNombreConSP(@Param("nombre") String nombre);

    List<Paciente> buscarPorObraSocialPaginado(@Param("p_obra_social") String obraSocial, @Param("p_limit") int limit, @Param("p_offset") int offset);
}
