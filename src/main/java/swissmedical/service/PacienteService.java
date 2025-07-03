package swissmedical.service;

import swissmedical.model.Paciente;
import java.util.List;

/**
 * Servicio para la gestión de pacientes.
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
     * Obtiene un paciente por su identificador único.
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
     * Elimina un paciente por su identificador único.
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
     * @return lista de pacientes que coinciden con el parámetro
     */
    List<Paciente> buscarPorNombreParcial(String nombre);
    /**
     * Actualiza los datos de un paciente existente.
     * @param id identificador del paciente a actualizar
     * @param paciente entidad Paciente con los nuevos datos
     * @return paciente actualizado
     */
    Paciente actualizarPaciente(Long id, Paciente paciente);
} 