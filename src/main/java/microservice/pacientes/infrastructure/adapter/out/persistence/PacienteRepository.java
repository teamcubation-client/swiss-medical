package microservice.pacientes.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Paciente
 * Proporciona metodos para acceder y consultar pacientes en la base de datos
 */
@Repository
public interface PacienteRepository extends JpaRepository<PacienteEntity, Long> {
    /**
     * Busca un paciente por su DNI
     * @param dni Documento Nacional de Identidad
     * @return Optional con el paciente encontrado, o vacio si no existe
     */
    Optional<PacienteEntity> findByDni(String dni);
    /**
     * Busca a los pacientes que contenga la cadena especificada del nombre, sin tener en cuenta mayusculas y minusculas
     * @param nombre parte o nombre completo a buscar
     * @return lista de pacientes que coinciden con el parametro
     */
    List<PacienteEntity> findByNombreContainingIgnoreCase(String nombre);

    @Query(value = "CALL buscar_paciente_por_dni(:dni)", nativeQuery = true)
    Optional<PacienteEntity> buscarByDni(@Param("dni") String dni);

    @Query(value = "CALL buscar_pacientes_por_nombre(:nombre)", nativeQuery = true)
    List<PacienteEntity> buscarByNombre(@Param("nombre") String nombre);

    @Query(value = "CALL buscar_pacientes_por_obra_social_paginado(:p_obra_social, :p_limit, :p_offset)", nativeQuery = true)
    List<PacienteEntity> buscarPorObraSocialPaginado(@Param("p_obra_social") String obraSocial, @Param("p_limit") int limit, @Param("p_offset") int offset);
} 