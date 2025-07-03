package swissmedical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swissmedical.model.Paciente;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Paciente
 * Proporciona metodos para acceder y consultar pacientes en la base de datos
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    /**
     * Busca un paciente por su DNI
     * @param dni Documento Nacional de Identidad
     * @return Optional con el paciente encontrado, o vacio si no existe
     */
    Optional<Paciente> findByDni(String dni);
    /**
     * Busca a los pacientes que contenga la cadena especificada del nombre, sin tener en cuenta mayusculas y minusculas
     * @param nombre parte o nombre completo a buscar
     * @return lista de pacientes que coinciden con el parametro
     */
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);
} 