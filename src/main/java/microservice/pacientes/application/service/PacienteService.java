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
import microservice.pacientes.shared.LoggerHelper;
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
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Creando paciente con DNI: {}", paciente.getDni());

        createChain.validate(paciente);
        LoggerHelper.info(logger, this, "Validaciones de creacion completadas");

        Paciente pacienteCreado = pacientePortOutWrite.save(paciente);
        LoggerHelper.info(logger, this, "Paciente creado exitosamente con ID: {}", pacienteCreado.getId());
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
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Buscando paciente por ID: {}", id);

        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseGet(() -> {
                    LoggerHelper.warn(logger, this, "No se encontro paciente con ID: {}", id);
                    throw PacienteNotFoundException.porId(id);
                });
        LoggerHelper.info(logger, this, "Paciente encontrado: {}", paciente);
        return paciente;
    }

    /**
     * Lista todos los pacientes registrados en el sistema
     * @return lista de pacientes
     */
    @Override
    @Transactional(readOnly = true)
    public List<Paciente> listarPacientesPorEstado(Boolean estado) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Listando pacientes con estado: {}", estado);

        List<Paciente> pacientes;

        if (estado == null) {
            pacientes = pacientePortOutRead.findAll();
        } else {
            pacientes = pacientePortOutRead.findByEstado(estado);
        }

        LoggerHelper.info(logger, this, "Se encontraron {} pacientes con estado {}", pacientes.size(), estado);
        LoggerHelper.info(logger, this, "Lista completa: {}", pacientes);
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
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Eliminando paciente con ID: {}", id);

        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseGet(() -> {
                    LoggerHelper.warn(logger, this, "Paciente con ID {} no encontrado", id);
                    throw PacienteNotFoundException.porId(id);
                });

        LoggerHelper.info(logger, this, "Paciente encontrado para eliminacion: {}", paciente);

        deleteChain.validate(paciente);
        LoggerHelper.info(logger, this, "Validaciones de eliminacion completadas");

        pacientePortOutWrite.deleteById(id);
        LoggerHelper.info(logger, this, "Paciente eliminado con exito: {}", id);
    }

    /**
     * Busca pacientes cuyo nombre contenga el string especificado
     * @param nombre parte o nombre completo a buscar
     * @return lista de pacientes que coinciden con el parametro
     */
    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarPorNombreParcial(String nombre) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Buscando pacientes por nombre parcial: '{}'", nombre);
        List<Paciente> pacientes = pacientePortOutRead.findByNombreContainingIgnoreCase(nombre);
        LoggerHelper.info(logger, this, "Se encontraron {} pacientes con nombre parcial '{}'", pacientes.size(), nombre);
        LoggerHelper.info(logger, this, "Pacientes encontrados: {}", pacientes);
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
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Actualizando paciente con ID: {}", id);
        LoggerHelper.info(logger, this, "Datos recibidos para actualizacion: {}", paciente);
        updateChain.validate(paciente);
        LoggerHelper.info(logger, this, "Validaciones de actualizacion completadas");

        Paciente pacienteActualizado = pacientePortOutWrite.update(paciente);
        LoggerHelper.info(logger, this, "Paciente actualizado con exito: {}", pacienteActualizado.getId());
        return pacienteActualizado;
    }

    @Override
    @Transactional
    public Paciente desactivarPaciente(Long id) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Desactivando paciente con ID: {}", id);

        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseGet(() -> {
                    LoggerHelper.warn(logger, this, "No se encontro paciente con ID: {}", id);
                    throw PacienteNotFoundException.porId(id);
                });
        LoggerHelper.info(logger, this, "Paciente encontrado: {}", paciente);

        paciente.setEstado(false);
        Paciente pacienteDesactivado = pacientePortOutWrite.save(paciente);
        LoggerHelper.info(logger, this, "Paciente desactivado: {}", paciente.getId());
        return pacienteDesactivado;
    }

    @Override
    @Transactional
    public Paciente activarPaciente(Long id) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Activando paciente con ID: {}", id);
        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseGet(() -> {
                    LoggerHelper.warn(logger, this, "No se encontró paciente con ID: {}", id);
                    throw PacienteNotFoundException.porId(id);
                });
        LoggerHelper.info(logger, this, "Paciente encontrado: {}", paciente);

        paciente.setEstado(true);
        Paciente pacienteActivado = pacientePortOutWrite.save(paciente);
        LoggerHelper.info(logger, this, "Paciente activado: {}", paciente.getId());
        return pacienteActivado;
    }

    @Override
    @Transactional (readOnly = true)
    public Paciente buscarByDni(String dni) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Buscando paciente por DNI: {}", dni);


        Paciente paciente = pacientePortOutRead.buscarByDni(dni)
                .orElseGet(() -> {
                    LoggerHelper.warn(logger, this, "No se encontro paciente con DNI: {}", dni);
                    throw PacienteNotFoundException.porDni(dni);
                });

        LoggerHelper.info(logger, this, "Paciente encontrado: {}", paciente);
        return paciente;
    }

    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarByNombre(String nombre) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Buscando pacientes por nombre: '{}'", nombre);
        List<Paciente> pacientes = pacientePortOutRead.buscarByNombre(nombre);
        if (pacientes == null || pacientes.isEmpty()) {
            LoggerHelper.warn(logger, this, "No se encontraron pacientes con nombre: '{}'", nombre);
            throw PacienteNotFoundException.porNombre(nombre);
        }
        LoggerHelper.info(logger, this, "Se encontraron {} pacientes: {}", pacientes.size(), pacientes);
        return pacientes;
    }

    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarPorObraSocialPaginado(String obraSocial, int limit, int offset) {
        LoggerHelper.entrada(logger, this);
        LoggerHelper.info(logger, this, "Buscando pacientes por obra social: '{}', limit: {}, offset: {}", obraSocial, limit, offset);

        List<Paciente> pacientes = pacientePortOutRead.buscarPorObraSocialPaginado(obraSocial, limit, offset);
        if (pacientes == null || pacientes.isEmpty()) {
            LoggerHelper.warn(logger, this, "No se encontraron pacientes con obra social: '{}'", obraSocial);
            throw PacienteNotFoundException.porObraSocial(obraSocial);
        }
        LoggerHelper.info(logger, this, "Se encontraron {} pacientes: {}", pacientes.size(), pacientes);
        return pacientes;
        }

}
