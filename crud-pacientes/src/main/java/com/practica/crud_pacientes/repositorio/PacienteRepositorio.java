package com.practica.crud_pacientes.repositorio;

import com.practica.crud_pacientes.modelo.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PacienteRepositorio extends JpaRepository<Paciente, Integer> {
    Paciente findByDni(String dni);
    Paciente findByNombre(String nombre);
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);
}
