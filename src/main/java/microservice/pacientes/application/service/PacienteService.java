package microservice.pacientes.application.service;

import lombok.RequiredArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.PacientePortInRead;
import microservice.pacientes.application.domain.port.in.PacientePortInWrite;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.domain.port.out.PacientePortOutWrite;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.infrastructure.validation.*;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PacienteService implements PacientePortInWrite, PacientePortInRead {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);
    
    private final PacientePortOutRead pacientePortOutRead;
    private final PacientePortOutWrite pacientePortOutWrite;

    //Cabeza de la cadena handlers
    private PacienteValidator createChain;
    private PacienteValidator deleteChain;
    private PacienteValidator updateChain;

    //arma la cadena
    @PostConstruct
    void initValidatorChain() {
        DniDuplicadoValidator dniDuplicadoCreate = new DniDuplicadoValidator(pacientePortOutRead);
        FechaAltaValidator fechaAltaCreate = new FechaAltaValidator();
        EmailFormatValidator emailFormatoCreate = new EmailFormatValidator();

        dniDuplicadoCreate.setNext(fechaAltaCreate);
        fechaAltaCreate.setNext(emailFormatoCreate);
        this.createChain = dniDuplicadoCreate;

        ExistePacienteValidator existePacienteDelete = new ExistePacienteValidator(pacientePortOutRead);
        EstadoInactivoValidator inactivoDelete = new EstadoInactivoValidator();

        existePacienteDelete.setNext(inactivoDelete);
        this.deleteChain = existePacienteDelete;

        ExistePacienteValidator existePacienteUpdate = new ExistePacienteValidator(pacientePortOutRead);
        DniDuplicadoValidator dniDuplicadoUpdate = new DniDuplicadoValidator(pacientePortOutRead);
        FechaAltaValidator fechaAltaUpdate = new FechaAltaValidator();
        EmailFormatValidator emailFormatoUpdate = new EmailFormatValidator();

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
        logger.info("Iniciando creacion de paciente con DNI: {}", paciente.getDni());
        logger.debug("Datos del paciente a crear: {}", paciente);
        
        try {
            createChain.validate(paciente);
            logger.debug("Validaciones completadas exitosamente");
            
            Paciente pacienteCreado = pacientePortOutWrite.save(paciente);
            logger.info("Paciente creado exitosamente con ID: {}", pacienteCreado.getId());
            return pacienteCreado;
        } catch (Exception e) {
            logger.error("Error al crear paciente con DNI {}: {}", paciente.getDni(), e.getMessage());
            throw e;
        }
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
        logger.info("Buscando paciente por ID: {}", id);
        
        try {
            Paciente paciente = pacientePortOutRead.findById(id)
                    .orElseThrow(() -> PacienteNotFoundException.porId(id));
            logger.debug("Paciente encontrado: {}", paciente);
            return paciente;
        } catch (PacienteNotFoundException e) {
            logger.warn("Paciente no encontrado con ID: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error al buscar paciente por ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * Lista todos los pacientes registrados en el sistema
     * @return lista de pacientes
     */
    @Override
    @Transactional (readOnly = true)
    public List<Paciente> listarPacientes() {
        logger.info("Iniciando listar todos los pacientes");
        
        try {
            List<Paciente> pacientes = pacientePortOutRead.findAll();
            logger.debug("Se encontraron {} pacientes", pacientes.size());
            return pacientes;
        } catch (Exception e) {
            logger.error("Error al listar pacientes: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Elimina un paciente por su identificador unico, con excepcion si no existe
     * @param id identificador del paciente a eliminar
     * @throws PacienteNotFoundException si no se encuentra el paciente
     */
    @Override
    @Transactional
    public void eliminarPaciente(Long id) {
        logger.info("Iniciando eliminacion de paciente con ID: {}", id);
        
        try {
            Paciente paciente = pacientePortOutRead.findById(id)
                    .orElseThrow(() -> PacienteNotFoundException.porId(id));
            logger.debug("Paciente a eliminar encontrado: {}", paciente);
            
            deleteChain.validate(paciente);
            logger.debug("Validaciones de eliminacion completadas");
            
            pacientePortOutWrite.deleteById(id);
            logger.info("Paciente eliminado exitosamente con ID: {}", id);
        } catch (PacienteNotFoundException e) {
            logger.warn("Paciente no encontrado para eliminar con ID: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error al eliminar paciente con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * Busca pacientes cuyo nombre contenga el string especificado
     * @param nombre parte o nombre completo a buscar
     * @return lista de pacientes que coinciden con el parametro
     */
    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarPorNombreParcial(String nombre) {
        logger.info("Buscando pacientes por nombre parcial: '{}'", nombre);
        
        try {
            List<Paciente> pacientes = pacientePortOutRead.findByNombreContainingIgnoreCase(nombre);
            logger.debug("Se encontraron {} pacientes con nombre que contiene '{}'", pacientes.size(), nombre);
            return pacientes;
        } catch (Exception e) {
            logger.error("Error al buscar pacientes por nombre '{}': {}", nombre, e.getMessage());
            throw e;
        }
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
        logger.info("Iniciando actualizacion de paciente con ID: {}", id);
        logger.debug("Datos del paciente a actualizar: {}", paciente);
        
        try {
            paciente.setId(id);
            updateChain.validate(paciente);
            logger.debug("Validaciones de actualizacion completadas");
            
            Paciente pacienteActualizado = pacientePortOutWrite.update(paciente);
            logger.info("Paciente actualizado exitosamente con ID: {}", id);
            return pacienteActualizado;
        } catch (Exception e) {
            logger.error("Error al actualizar paciente con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Paciente desactivarPaciente(Long id) {
        logger.info("Desactivando paciente con ID: {}", id);
        
        try {
            Paciente paciente = pacientePortOutRead.findById(id)
                    .orElseThrow(() -> PacienteNotFoundException.porId(id));
            logger.debug("Paciente encontrado para desactivar: {}", paciente);
            
            paciente.setEstado(false);
            Paciente pacienteDesactivado = pacientePortOutWrite.save(paciente);
            logger.info("Paciente desactivado exitosamente con ID: {}", id);
            return pacienteDesactivado;
        } catch (PacienteNotFoundException e) {
            logger.warn("Paciente no encontrado para desactivar con ID: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error al desactivar paciente con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Paciente activarPaciente(Long id) {
        logger.info("Activando paciente con ID: {}", id);
        
        try {
            Paciente paciente = pacientePortOutRead.findById(id)
                    .orElseThrow(() -> PacienteNotFoundException.porId(id));
            logger.debug("Paciente encontrado para activar: {}", paciente);
            
            paciente.setEstado(true);
            Paciente pacienteActivado = pacientePortOutWrite.save(paciente);
            logger.info("Paciente activado exitosamente con ID: {}", id);
            return pacienteActivado;
        } catch (PacienteNotFoundException e) {
            logger.warn("Paciente no encontrado para activar con ID: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error al activar paciente con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional (readOnly = true)
    public Paciente buscarByDni(String dni) {
        logger.info("Buscando paciente por DNI: {}", dni);
        
        try {
            Paciente paciente = pacientePortOutRead.buscarByDni(dni)
                    .orElseThrow(() -> PacienteNotFoundException.porDni(dni));
            logger.debug("Paciente encontrado por DNI: {}", paciente);
            return paciente;
        } catch (PacienteNotFoundException e) {
            logger.warn("Paciente no encontrado con DNI: {}", dni);
            throw e;
        } catch (Exception e) {
            logger.error("Error al buscar paciente por DNI {}: {}", dni, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarByNombre(String nombre) {
        logger.info("Buscando pacientes por nombre exacto: '{}'", nombre);
        
        try {
            List<Paciente> paciente = pacientePortOutRead.buscarByNombre(nombre);
            if (paciente == null || paciente.isEmpty()) {
                logger.warn("No se encontraron pacientes con nombre: '{}'", nombre);
                throw PacienteNotFoundException.porNombre(nombre);
            }
            logger.debug("Se encontraron {} pacientes con nombre '{}'", paciente.size(), nombre);
            return paciente;
        } catch (PacienteNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al buscar pacientes por nombre '{}': {}", nombre, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarPorObraSocialPaginado(String obraSocial, int limit, int offset) {
        logger.info("Buscando pacientes por obra social: '{}' con limit: {} y offset: {}", obraSocial, limit, offset);
        
        try {
            List<Paciente> pacientes = pacientePortOutRead.buscarPorObraSocialPaginado(obraSocial, limit, offset);
            if (pacientes == null || pacientes.isEmpty()) {
                logger.warn("No se encontraron pacientes con obra social: '{}'", obraSocial);
                throw PacienteNotFoundException.porObraSocial(obraSocial);
            }
            logger.debug("Se encontraron {} pacientes con obra social '{}'", pacientes.size(), obraSocial);
            return pacientes;
        } catch (PacienteNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al buscar pacientes por obra social '{}': {}", obraSocial, e.getMessage());
            throw e;
        }
    }

}
