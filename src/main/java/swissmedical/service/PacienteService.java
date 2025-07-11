package swissmedical.service;

import swissmedical.model.Paciente;
import java.util.List;
import swissmedical.dto.PacienteDTO;

/**
 * Servicio para la gestion de pacientes.
 * Define las operaciones principales para crear, consultar, actualizar y eliminar pacientes.
 */
public interface PacienteService {
    /**
     * Crea un nuevo paciente en el sistema.
     * @param paciente entidad Paciente a crear
     * @return paciente creado
     */
    Paciente crearPaciente(Paciente paciente);
    /**
     * Obtiene un paciente por su identificador unico.
     * @param id identificador del paciente
     * @return paciente encontrado
     */
    Paciente obtenerPacientePorId(Long id);
    /**
     * Lista todos los pacientes registrados en el sistema.
     * @return lista de pacientes
     */
    List<Paciente> listarPacientes();
    /**
     * Elimina un paciente por su identificador unico.
     * @param id identificador del paciente a eliminar
     */
    void eliminarPaciente(Long id);
    /**
     * Busca un paciente por su DNI.
     * @param dni Documento Nacional de Identidad
     * @return paciente encontrado
     */
    Paciente buscarPorDni(String dni);
    /**
     * Busca pacientes cuyo nombre contenga la cadena especificada.
     * @param nombre parte o nombre completo a buscar
     * @return lista de pacientes que coinciden con el parametro
     */
    List<Paciente> buscarPorNombreParcial(String nombre);
    /**
     * Actualiza los datos de un paciente existente.
     * @param id identificador del paciente a actualizar
     * @param paciente entidad Paciente con los nuevos datos
     * @return paciente actualizado
     */
    Paciente actualizarPaciente(Long id, Paciente paciente);

    /**
     * Desactiva el estado de un paciente existente.
     * @param id identificador del paciente a desactivar
     * @return paciente desactivado
     */
    Paciente desactivarPaciente(Long id);

    /**
     * Activa el estado de un paciente existente.
     * @param id identificador del paciente a activar
     * @return paciente activado
     */
    Paciente activarPaciente(Long id);

    /**
     * Busca un paciente por su DNI usando stored procedure
     * @param dni Documento Nacional de Identidad
     * @return PacienteDTO encontrado
     */
    PacienteDTO buscarPorDniConSP(String dni);

    /**
     * Busca pacientes cuyo nombre contenga la cadena especificada usando stored procedure
     * @param nombre parte o nombre completo a buscar
     * @return lista de PacienteDTO que coinciden con el parametro
     */
    List<PacienteDTO> buscarPorNombreConSP(String nombre);

    /**
     * Busca pacientes por obra social con paginacion usando stored procedure
     * @param obraSocial nombre de la obra social
     * @param limit cantidad maxima de resultados
     * @param offset desplazamiento de resultados
     * @return lista de PacienteDTO
     */
    List<PacienteDTO> buscarPorObraSocialPaginado(String obraSocial, int limit, int offset);
}