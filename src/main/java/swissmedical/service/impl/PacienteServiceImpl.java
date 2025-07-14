package swissmedical.service.impl;

import swissmedical.model.Paciente;
import swissmedical.repository.PacienteRepository;
import swissmedical.service.PacienteService;
import java.util.List;
import org.springframework.stereotype.Service;
import swissmedical.exception.PacienteDuplicadoException;
import swissmedical.exception.PacienteNotFoundException;
import swissmedical.dto.PacienteDTO;
import swissmedical.mapper.PacienteMapper;
import java.util.stream.Collectors;

/**
 * Implementacion del servicio de pacientes
 * Contiene la logica de negocio para crear, consultar, actualizar y eliminar pacientes
 */
@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    public PacienteServiceImpl(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    /**
     * Crea un nuevo paciente en el sistema, con excepcion si el DNI ya existe
     * @param paciente entidad Paciente a crear
     * @return paciente creado
     * @throws PacienteDuplicadoException si ya existe un paciente con el mismo DNI
     */
    @Override
    public Paciente crearPaciente(Paciente paciente) {
        pacienteRepository.findByDni(paciente.getDni())
                .ifPresent(p -> {
                    throw new PacienteDuplicadoException(paciente.getDni());
                });

        return pacienteRepository.save(paciente);
    }

    /**
     * Obtiene un paciente por su identificador unico, con excepcion si no existe
     * @param id identificador del paciente
     * @return paciente encontrado
     * @throws PacienteNotFoundException si no se encuentra el paciente
     */
    @Override
    public Paciente obtenerPacientePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));
    }

    /**
     * Lista todos los pacientes registrados en el sistema
     * @return lista de pacientes
     */
    @Override
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    /**
     * Elimina un paciente por su identificador unico, con excepcion si no existe
     * @param id identificador del paciente a eliminar
     * @throws PacienteNotFoundException si no se encuentra el paciente
     */
    @Override
    public void eliminarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));

        pacienteRepository.delete(paciente);
    }

    /**
     * Busca un paciente por su DNI
     * @param dni Documento Nacional de Identidad
     * @return paciente encontrado o null si no existe
     */
    @Override
    public Paciente buscarPorDni(String dni) {
        Paciente paciente = pacienteRepository.findByDni(dni).orElse(null);
        return paciente;
    }

    /**
     * Busca pacientes cuyo nombre contenga el string especificado
     * @param nombre parte o nombre completo a buscar
     * @return lista de pacientes que coinciden con el parametro
     */
    @Override
    public List<Paciente> buscarPorNombreParcial(String nombre) {
        List<Paciente> pacientes = pacienteRepository.findByNombreContainingIgnoreCase(nombre);
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
        Paciente existente = pacienteRepository.findById(id)
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
        return pacienteRepository.save(existente);
    }

    @Override
    public Paciente desactivarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));
        paciente.setEstado(false);
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente activarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));
        paciente.setEstado(true);
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente buscarPorDniConSP(String dni) {
        return pacienteRepository.buscarPorDniConSP(dni)
            .orElseThrow(() -> PacienteNotFoundException.porDni(dni));
    }

    @Override
    public List<Paciente> buscarPorNombreConSP(String nombre) {
        List<Paciente> paciente = pacienteRepository.buscarPorNombreConSP(nombre);
        if (paciente == null || paciente.isEmpty()) {
            throw PacienteNotFoundException.porNombre(nombre);
        }
        return paciente;
    }

    @Override
    public List<Paciente> buscarPorObraSocialPaginado(String obraSocial, int limit, int offset) {
        List<Paciente> paciente = pacienteRepository.buscarPorObraSocialPaginado(obraSocial, limit, offset);
        if (paciente == null || paciente.isEmpty()) {
            throw PacienteNotFoundException.porObraSocial(obraSocial);
        }
        return paciente;
    }

} 