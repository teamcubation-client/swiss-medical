package com.practica.crud_pacientes.repository;

import com.practica.crud_pacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    Paciente findByDni(String dni);
    Paciente findByNombre(String nombre);
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);

    @Procedure(procedureName = "buscar_paciente_por_dni")
    Paciente getByDniFromSP(@Param("p_dni") String dni);

    @Query(value = "CALL buscar_pacientes_por_nombre(:p_nombre)", nativeQuery = true)
    List<Paciente> getPacientesByNombreFromSP(@Param("p_nombre") String nombre);

    @Query(value = "CALL buscar_pacientes_por_obra_social_paginado(:p_obra_social, :p_limit, :p_offset)", nativeQuery = true)
    List<Paciente> getPacietesbyObraSocialFromSP(
            @Param("p_obra_social") String obraSocial,
            @Param("p_limit") int limite,
            @Param("p_offset") int off
            );
}
