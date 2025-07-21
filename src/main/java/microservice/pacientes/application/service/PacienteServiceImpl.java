package microservice.pacientes.application.service;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.PacientePortIn;
import microservice.pacientes.application.domain.port.out.PacientePortOut;
import microservice.pacientes.shared.PacienteDuplicadoException;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@AllArgsConstructor
@Transactional
public class PacienteServiceImpl implements PacientePortIn {

    private final PacientePortOut pacientePortOut;
    /**
     * Crea un nuevo paciente en el sistema, con excepcion si el DNI ya existe
     * @param paciente entidad Paciente a crear
     * @return paciente creado
     * @throws PacienteDuplicadoException si ya existe un paciente con el mismo DNI
     */
    @Override
    public Paciente crearPaciente(Paciente paciente) {
        pacientePortOut.findByDni(paciente.getDni())
                .ifPresent(p -> {
                    throw new PacienteDuplicadoException(paciente.getDni());
                });

        return pacientePortOut.save(paciente);
    }

    /**
     * Obtiene un paciente por su identificador unico, con excepcion si no existe
     * @param id identificador del paciente
     * @return paciente encontrado
     * @throws PacienteNotFoundException si no se encuentra el paciente
     */
    @Override
    public Paciente obtenerPacientePorId(Long id) {
        return pacientePortOut.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));
    }

    /**
     * Lista todos los pacientes registrados en el sistema
     * @return lista de pacientes
     */
    @Override
    public List<Paciente> listarPacientes() {
        return pacientePortOut.findAll();
    }

    /**
     * Elimina un paciente por su identificador unico, con excepcion si no existe
     * @param id identificador del paciente a eliminar
     * @throws PacienteNotFoundException si no se encuentra el paciente
     */
    @Override
    public void eliminarPaciente(Long id) {
        Paciente paciente = pacientePortOut.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));

        pacientePortOut.deleteById(id);
    }

    /**
     * Busca un paciente por su DNI
     * @param dni Documento Nacional de Identidad
     * @return paciente encontrado o null si no existe
     */
    @Override
    public Paciente buscarPorDni(String dni) {
        Paciente paciente = pacientePortOut.findByDni(dni).orElse(null);
        return paciente;
    }

    /**
     * Busca pacientes cuyo nombre contenga el string especificado
     * @param nombre parte o nombre completo a buscar
     * @return lista de pacientes que coinciden con el parametro
     */
    @Override
    public List<Paciente> buscarPorNombreParcial(String nombre) {
        List<Paciente> pacientes = pacientePortOut.findByNombreContainingIgnoreCase(nombre);
        return pacientes;
    }

    /**
     * Actualiza los datos de un paciente existente, con excepcion si no existe
     * @param id identificador del paciente a actualizar
     * @param paciente entidad Paciente con los nuevos datos
     * @return paciente actualizado
     * @throws PacienteNotFoundException si no se encuentra el paciente
     */
    @Override
    public Paciente actualizarPaciente(Long id, Paciente paciente) {
        Paciente existente = pacientePortOut.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));

        if (paciente.getNombre() != null) {
            existente.setNombre(paciente.getNombre());
        }
        if (paciente.getApellido() != null) {
            existente.setApellido(paciente.getApellido());
        }
        if (paciente.getDni() != null) {
            existente.setDni(paciente.getDni());
        }
        if (paciente.getObraSocial() != null) {
            existente.setObraSocial(paciente.getObraSocial());
        }
        if (paciente.getEmail() != null) {
            existente.setEmail(paciente.getEmail());
        }
        if (paciente.getTelefono() != null) {
            existente.setTelefono(paciente.getTelefono());
        }
        if (paciente.getTipoPlanObraSocial() != null) {
            existente.setTipoPlanObraSocial(paciente.getTipoPlanObraSocial());
        }
        if (paciente.getFechaAlta() != null) {
            existente.setFechaAlta(paciente.getFechaAlta());
        }
        existente.setEstado(paciente.isEstado());
        return pacientePortOut.save(existente);
    }

    @Override
    public Paciente desactivarPaciente(Long id) {
        Paciente paciente = pacientePortOut.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));
        paciente.setEstado(false);
        return pacientePortOut.save(paciente);
    }

    @Override
    public Paciente activarPaciente(Long id) {
        Paciente paciente = pacientePortOut.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));
        paciente.setEstado(true);
        return pacientePortOut.save(paciente);
    }

    @Override
    public Paciente buscarByDni(String dni) {
        return pacientePortOut.buscarPorDniConSP(dni)
                .orElseThrow(() -> PacienteNotFoundException.porDni(dni));
    }

    @Override
    public List<Paciente> buscarByNombre(String nombre) {
        List<Paciente> paciente = pacientePortOut.buscarPorNombreConSP(nombre);
        if (paciente == null || paciente.isEmpty()) {
            throw PacienteNotFoundException.porNombre(nombre);
        }
        return paciente;
    }

    @Override
    public List<Paciente> buscarPorObraSocialPaginado(String obraSocial, int limit, int offset) {
        List<Paciente> paciente = pacientePortOut.buscarPorObraSocialPaginado(obraSocial, limit, offset);
        if (paciente == null || paciente.isEmpty()) {
            throw PacienteNotFoundException.porObraSocial(obraSocial);
        }
        return paciente;
    }

}
