package microservice.pacientes.application.domain.port.in;

import microservice.pacientes.application.domain.model.Paciente;

import java.util.List;

public interface PacientePortInRead {

    Paciente obtenerPacientePorId(Long id);

    List<Paciente> listarPacientes();

    Paciente buscarPorDni(String dni);

    List<Paciente> buscarPorNombreParcial(String nombre);

    /**
     * Busca un paciente por su DNI usando stored procedure.
     * @param dni Documento Nacional de Identidad
     * @return paciente encontrado
     */
    Paciente buscarByDni(String dni);

    /**
     * Busca pacientes cuyo nombre contenga la cadena especificada usando stored procedure.
     * @param nombre parte o nombre completo a buscar
     * @return lista de pacientes que coinciden con el parámetro
     */
    List<Paciente> buscarByNombre(String nombre);

    /**
     * Busca pacientes por obra social con paginación usando stored procedure.
     * @param obraSocial nombre de la obra social
     * @param limit      cantidad máxima de resultados
     * @param offset     desplazamiento de resultados
     * @return lista de pacientes
     */
    List<Paciente> buscarPorObraSocialPaginado(String obraSocial, int limit, int offset);
}
