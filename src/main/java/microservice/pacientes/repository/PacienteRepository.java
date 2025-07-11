package microservice.pacientes.repository;

import microservice.pacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByDni(String dni);
    boolean existsByDni(String dni);
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);

    @Query(value = "CALL buscar_paciente_por_dni(:dni)", nativeQuery = true)
    Optional<Paciente> findByDniSP(@Param("dni") String dni);

    @Procedure(name = "findByNombreSP")
    Optional<Paciente> findByNombreSP(@Param("p_nombre") String nombre);

    @Procedure(name = "findByObraSocialSP")
    List<Paciente> findByObraSocialSP(@Param("p_obra_social") String obraSocial, @Param("p_limit") int limit, @Param("p_offset") int offset);


}
