package com.teamcubation.api.pacientes.repository;

import com.teamcubation.api.pacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IPacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByDni(String dni);

    @Query("SELECT p FROM Paciente p WHERE (:dni IS NULL OR p.dni = :dni) AND (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    List<Paciente> buscarTodos(@Param("dni") String dni, @Param("nombre") String nombre);

    @Query(value = "CALL buscar_paciente_por_dni(:dni)", nativeQuery = true)
    Optional<Paciente> findByDniSP(@Param("dni") String dni);

    @Query(value = "CALL buscar_pacientes_por_nombre(:nombre)", nativeQuery = true)
    List<Paciente> findByNombreSP(@Param("nombre") String nombre);

    @Query(value = "CALL buscar_pacientes_por_obra_social_paginado(:obraSocial, :limit, :offset)", nativeQuery = true)
    List<Paciente> findByObraSocialPaginadoSP(
            @Param("obraSocial") String obraSocial,
            @Param("limit") int limit,
            @Param("offset") int offset);
}
