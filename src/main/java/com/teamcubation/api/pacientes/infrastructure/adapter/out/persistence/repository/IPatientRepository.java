package com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.repository;

import com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPatientRepository extends JpaRepository<PatientEntity, Long> {

    @Query("SELECT p FROM PatientEntity p " +
            "WHERE (:dni IS NULL OR p.dni = :dni) " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<PatientEntity> search(@Param("dni") String dni, @Param("name") String name);

    @Query(value = "CALL buscar_paciente_por_dni(:dni)", nativeQuery = true)
    Optional<PatientEntity> findByDni(@Param("dni") String dni);

    @Query(value = "CALL buscar_pacientes_por_nombre(:name)", nativeQuery = true)
    List<PatientEntity> findByName(@Param("name") String name);

    @Query(value = "CALL buscar_pacientes_por_obra_social_paginado(:obraSocial, :limit, :offset)", nativeQuery = true)
    List<PatientEntity> findByHealthInsuranceProvider(
            @Param("obraSocial") String healthInsuranceProvider,
            @Param("limit") int limit,
            @Param("offset") int offset);
}