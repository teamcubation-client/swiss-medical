package microservice.pacientes.application.service;

import lombok.RequiredArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.PacientePortInRead;
import microservice.pacientes.application.domain.port.in.PacientePortInWrite;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.domain.port.out.PacientePortOutWrite;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.infrastructure.validation.DniDuplicadoValidator;
import microservice.pacientes.infrastructure.validation.FechaAltaValidator;
import microservice.pacientes.infrastructure.validation.EmailFormatValidator;
import microservice.pacientes.infrastructure.validation.ExistePacienteValidator;
import microservice.pacientes.infrastructure.validation.EstadoInactivoValidator;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PacienteService implements PacientePortInWrite, PacientePortInRead {

    private final LoggerPort logger;
    private final PacientePortOutRead pacientePortOutRead;
    private final PacientePortOutWrite pacientePortOutWrite;

    //Cabeza de la cadena handlers
    private PacienteValidator createChain;
    private PacienteValidator deleteChain;
    private PacienteValidator updateChain;

    //arma la cadena
    @PostConstruct
    void initValidatorChain() {

        DniDuplicadoValidator dniDuplicadoCreate = new DniDuplicadoValidator(pacientePortOutRead, logger);
        FechaAltaValidator fechaAltaCreate = new FechaAltaValidator(logger);
        EmailFormatValidator emailFormatoCreate = new EmailFormatValidator(logger);

        dniDuplicadoCreate.setNext(fechaAltaCreate);
        fechaAltaCreate.setNext(emailFormatoCreate);
        this.createChain = dniDuplicadoCreate;

        ExistePacienteValidator existePacienteDelete = new ExistePacienteValidator(pacientePortOutRead,logger);
        EstadoInactivoValidator inactivoDelete = new EstadoInactivoValidator(logger);

        existePacienteDelete.setNext(inactivoDelete);
        this.deleteChain = existePacienteDelete;

        ExistePacienteValidator existePacienteUpdate = new ExistePacienteValidator(pacientePortOutRead,logger);
        DniDuplicadoValidator dniDuplicadoUpdate = new DniDuplicadoValidator(pacientePortOutRead,logger);
        FechaAltaValidator fechaAltaUpdate = new FechaAltaValidator(logger);
        EmailFormatValidator emailFormatoUpdate = new EmailFormatValidator(logger);

        existePacienteUpdate.setNext(dniDuplicadoUpdate);
        dniDuplicadoUpdate.setNext(emailFormatoUpdate);
        emailFormatoUpdate.setNext(fechaAltaUpdate);
        this.updateChain = existePacienteUpdate;
    }


    /**
     * Crea un nuevo paciente en el sistema, utilizando patron Chain of Responsibility
     */
    @Override
    @Transactional
    public Paciente crearPaciente(Paciente paciente) {
        logger.entrada();
        logger.info("[PacienteService] Creando paciente con DNI: {}", paciente.getDni());

        createChain.validate(paciente);
        logger.info("[PacienteService] Validaciones de creacion completadas");

        Paciente pacienteCreado = pacientePortOutWrite.save(paciente);
        logger.info("Paciente creado exitosamente con ID: {}", pacienteCreado.getId());
        return pacienteCreado;
    }

    /**
     * Obtiene un paciente por su identificador unico, con excepcion si no existe
     * @param id identificador del paciente
     * @return paciente encontrado
     * @throws PacienteNotFoundException si no se encuentra el paciente
     */
    @Override
    @Transactional (readOnly = true)
    public Paciente obtenerPacientePorId(Long id) {
        logger.entrada();
        logger.info("[PacienteService] Buscando paciente por ID: {}", id);

        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseGet(() -> {
                    logger.warn("[PacienteService] No se encontro paciente con ID: {}", id);
                    throw PacienteNotFoundException.porId(id);
                });
        logger.info("[PacienteService] Paciente encontrado: {}", paciente);
        return paciente;
    }

    /**
     * Lista todos los pacientes registrados en el sistema
     * @return lista de pacientes
     */
    @Override
    @Transactional(readOnly = true)
    public List<Paciente> listarPacientesPorEstado(Boolean estado) {
        logger.entrada();
        logger.info("[PacienteService] Listando pacientes con estado: {}", estado);

        List<Paciente> pacientes;

        if (estado == null) {
            pacientes = pacientePortOutRead.findAll();
        } else {
            pacientes = pacientePortOutRead.findByEstado(estado);
        }

        logger.info("[PacienteService] Se encontraron {} pacientes con estado {}", pacientes.size(), estado);
        logger.info("[PacienteService] Lista completa: {}", pacientes);
        return pacientes;
    }
    /**
     * Elimina un paciente por su identificador unico, con excepcion si no existe
     * @param id identificador del paciente a eliminar
     * @throws PacienteNotFoundException si no se encuentra el paciente
     */
    @Override
    @Transactional
    public void eliminarPaciente(Long id) {
        logger.entrada();
        logger.info("[PacienteService] Eliminando paciente con ID: {}", id);

        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseGet(() -> {
                    logger.warn("[PacienteService] Paciente con ID {} no encontrado", id);
                    throw PacienteNotFoundException.porId(id);
                });

        logger.info("[PacienteService] Paciente encontrado para eliminación: {}", paciente);

        deleteChain.validate(paciente);
        logger.info("[PacienteService] Validaciones de eliminacion completadas");

        pacientePortOutWrite.deleteById(id);
        logger.info("[PacienteService] Paciente eliminado con éxito: {}", id);
    }

    /**
     * Busca pacientes cuyo nombre contenga el string especificado
     * @param nombre parte o nombre completo a buscar
     * @return lista de pacientes que coinciden con el parametro
     */
    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarPorNombreParcial(String nombre) {
        logger.entrada();
        logger.info("[PacienteService] Buscando pacientes por nombre parcial: '{}'", nombre);
        List<Paciente> pacientes = pacientePortOutRead.findByNombreContainingIgnoreCase(nombre);
        logger.info("[PacienteService] Se encontraron {} pacientes con nombre parcial '{}'", pacientes.size(), nombre);
        logger.info("[PacienteService] Pacientes encontradados: {}", pacientes);
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
    @Transactional
    public Paciente actualizarPaciente(Long id, Paciente paciente) {
        logger.entrada();
        logger.info("[PacienteService] Actualizando paciente con ID: {}", id);
        logger.info("[PacienteService] Datos recibidos para actualizacion: {}", paciente);
        paciente.setId(id);
        updateChain.validate(paciente);
        logger.info("[PacienteService] Validaciones de actualizacion completadas");

        Paciente pacienteActualizado = pacientePortOutWrite.update(paciente);
        logger.info("[PacienteService] Paciente actualizado con exito: {}", pacienteActualizado.getId());
        return pacienteActualizado;
    }

    @Override
    @Transactional
    public Paciente desactivarPaciente(Long id) {
        logger.entrada();
        logger.info("[PacienteService] Desactivando paciente con ID: {}", id);

        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseGet(() -> {
                    logger.warn("[PacienteService] No se encontro paciente con ID: {}", id);
                    throw PacienteNotFoundException.porId(id);
                });
        logger.info("[PacienteService] Paciente encontrado: {}", paciente);

        paciente.setEstado(false);
        Paciente pacienteDesactivado = pacientePortOutWrite.save(paciente);
        logger.info("[PacienteService] Paciente desactivado: {}", paciente.getId());
        return pacienteDesactivado;
    }

    @Override
    @Transactional
    public Paciente activarPaciente(Long id) {
        logger.entrada();
        logger.info("[PacienteService] Activando paciente con ID: {}", id);
        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseGet(() -> {
                    logger.warn("[PacienteService] No se encontro paciente con ID: {}", id);
                    throw PacienteNotFoundException.porId(id);
                });
        logger.info("[PacienteService] Paciente encontrado: {}", paciente);

        paciente.setEstado(true);
        Paciente pacienteActivado = pacientePortOutWrite.save(paciente);
        logger.info("[PacienteService] Paciente activado: {}", paciente.getId());
        return pacienteActivado;
    }

    @Override
    @Transactional (readOnly = true)
    public Paciente buscarByDni(String dni) {
        logger.entrada();
        logger.info("[PacienteService] Buscando paciente por DNI: {}", dni);


        Paciente paciente = pacientePortOutRead.buscarByDni(dni)
                .orElseGet(() -> {
                    logger.warn("[PacienteService] No se encontro paciente con DNI: {}", dni);
                    throw PacienteNotFoundException.porDni(dni);
                });
        logger.info("[PacienteService] Paciente encontrado: {}", paciente);
        return paciente;
    }

    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarByNombre(String nombre) {
        logger.entrada();
        logger.info("[PacienteService] Buscando pacientes por nombre: '{}'", nombre);
        List<Paciente> pacientes = pacientePortOutRead.buscarByNombre(nombre);
        if (pacientes == null || pacientes.isEmpty()) {
            logger.warn("[PacienteService] No se encontraron pacientes con nombre: '{}'", nombre);
            throw PacienteNotFoundException.porNombre(nombre);
        }
        logger.info("[PacienteService] Se encontraron {} pacientes: {}", pacientes.size(), pacientes);
        return pacientes;
    }

    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarPorObraSocialPaginado(String obraSocial, int limit, int offset) {
        logger.entrada();
        logger.info("[PacienteService] Buscando pacientes por obra social: '{}', limit: {}, offset: {}", obraSocial, limit, offset);

        List<Paciente> pacientes = pacientePortOutRead.buscarPorObraSocialPaginado(obraSocial, limit, offset);
        if (pacientes == null || pacientes.isEmpty()) {
            logger.warn("[PacienteService] No se encontraron pacientes con obra social: '{}'", obraSocial);
            throw PacienteNotFoundException.porObraSocial(obraSocial);
        }
        logger.info("[PacienteService] Se encontraron {} pacientes: {}", pacientes.size(), pacientes);
        return pacientes;
        }

}
