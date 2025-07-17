package com.practica.crud_pacientes.infrastructure.adapter.out.persistence;

import com.practica.crud_pacientes.infrastructure.adapter.out.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PacienteJpaRepository extends JpaRepository<PacienteEntity, Integer> {
    @Procedure(procedureName = "buscar_paciente_por_dni")
    PacienteEntity getByDni(@Param("p_dni") String dni);

    @Procedure(procedureName = "buscar_pacientes_por_nombre")
    List<PacienteEntity> getPacientesByNombre(@Param("p_nombre") String nombre);

    @Procedure(procedureName = "buscar_pacientes_por_obra_social_paginado")
    List<PacienteEntity> getPacietesbyObraSocial(
            @Param("p_obra_social") String obraSocial,
            @Param("p_limit") int limite,
            @Param("p_offset") int off
    );
}
