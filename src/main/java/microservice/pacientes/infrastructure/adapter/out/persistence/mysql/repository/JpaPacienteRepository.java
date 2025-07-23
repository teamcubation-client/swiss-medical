package microservice.pacientes.infrastructure.adapter.out.persistence.mysql.repository;

import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPacienteRepository extends JpaRepository<PacienteEntity, String> {

    Optional<PacienteEntity> findByDni(String dni);
    boolean existsByDni(String dni);
    List<PacienteEntity> findByNombreContainingIgnoreCase(String nombre);

    @Query(value = "CALL buscar_paciente_por_dni(:dni)", nativeQuery = true)
    Optional<PacienteEntity> findByDniSP(@Param("dni") String dni);

    @Procedure(procedureName = "buscar_pacientes_por_nombre")
    Optional<PacienteEntity> findByNombreSP(@Param("p_nombre") String nombre);

    @Procedure(procedureName = "buscar_pacientes_por_obra_social_paginado")
    List<PacienteEntity> findByObraSocialSP(@Param("p_obraSocial") String obraSocial, @Param("p_limit") int limit, @Param("p_offset") int offset);


}
