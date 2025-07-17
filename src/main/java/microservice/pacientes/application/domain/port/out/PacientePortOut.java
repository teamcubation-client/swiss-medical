package microservice.pacientes.application.domain.port.out;

import microservice.pacientes.application.domain.model.Paciente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacientePortOut {
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

    /**
     * Busca un paciente por su ID
     * @param id identificador del paciente
     * @return Optional con el paciente encontrado, o vacio si no existe
     */
    Optional<Paciente> findById(Long id);

    /**
     * Guarda un nuevo paciente o actualiza uno existente
     * @param paciente paciente a guardar
     * @return paciente guardado
     */
    Paciente save(Paciente paciente);

    /**
     * Actualiza un paciente existente
     * @param paciente paciente a actualizar
     * @return paciente actualizado
     */
    Paciente update(Paciente paciente);

    /**
     * Elimina un paciente por su id
     * @param id identificador del paciente
     */
    void deleteById(long id);

    /**
     * Obtiene todos los pacientes
     * @return lista de pacientes
     */
    List<Paciente> findAll();

    Optional<Paciente> buscarPorDniConSP(@Param("dni") String dni);

    List<Paciente> buscarPorNombreConSP(@Param("nombre") String nombre);

    List<Paciente> buscarPorObraSocialPaginado(@Param("p_obra_social") String obraSocial, @Param("p_limit") int limit, @Param("p_offset") int offset);
}
