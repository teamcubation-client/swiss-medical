package microservice.pacientes.application.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.PacientePortInRead;
import microservice.pacientes.application.domain.port.in.PacientePortInWrite;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.domain.port.out.PacientePortOutWrite;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.infrastructure.validation.*;
import microservice.pacientes.shared.PacienteDuplicadoException;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacientePortInWrite, PacientePortInRead {

    private final PacientePortOutRead pacientePortOutRead;
    private final PacientePortOutWrite pacientePortOutWrite;

    //Cabeza de la cadena handlers
    private PacienteValidator createChain;
    private PacienteValidator deleteChain;
    private PacienteValidator updateChain;

    //handlers concretos inyectados
    private final DniDuplicadoValidator dniValidator;
    private final FechaAltaValidator fechaValidator;
    private final EmailFormatValidator emailValidator;
    private final EstadoInactivoValidator estadoInactivoValidator;
    private final ExistePacienteValidator existePacienteValidator;

    /**
     * Arma la cadena: dni -> fecha -> email
     */
    @PostConstruct
    private void initValidatorChain() {
        dniValidator.setNext(fechaValidator);
        fechaValidator.setNext(emailValidator);
        this.createChain = dniValidator;


        existePacienteValidator.setNext(estadoInactivoValidator);
        this.deleteChain = existePacienteValidator;


        existePacienteValidator.setNext(dniValidator);
        dniValidator.setNext(emailValidator);
        emailValidator.setNext(fechaValidator);
        this.updateChain = existePacienteValidator;
    }


    /**
     * Crea un nuevo paciente en el sistema, utilizando patron Chain of Responsibility
     */
    @Override
    @Transactional
    public Paciente crearPaciente(Paciente paciente) {
        createChain.validate(paciente);
        return pacientePortOutWrite.save(paciente);
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
        return pacientePortOutRead.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));
    }

    /**
     * Lista todos los pacientes registrados en el sistema
     * @return lista de pacientes
     */
    @Override
    @Transactional (readOnly = true)
    public List<Paciente> listarPacientes() {
        return pacientePortOutRead.findAll();
    }

    /**
     * Elimina un paciente por su identificador unico, con excepcion si no existe
     * @param id identificador del paciente a eliminar
     * @throws PacienteNotFoundException si no se encuentra el paciente
     */
    @Override
    @Transactional
    public void eliminarPaciente(Long id) {
        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));
        deleteChain.validate(paciente);
        pacientePortOutWrite.deleteById(id);
    }

    /**
     * Busca pacientes cuyo nombre contenga el string especificado
     * @param nombre parte o nombre completo a buscar
     * @return lista de pacientes que coinciden con el parametro
     */
    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarPorNombreParcial(String nombre) {
        List<Paciente> pacientes = pacientePortOutRead.findByNombreContainingIgnoreCase(nombre);
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
        paciente.setId(id);
        updateChain.validate(paciente);
        return pacientePortOutWrite.update(paciente);
    }

    @Override
    @Transactional
    public Paciente desactivarPaciente(Long id) {
        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));
        paciente.setEstado(false);
        return pacientePortOutWrite.save(paciente);
    }

    @Override
    @Transactional
    public Paciente activarPaciente(Long id) {
        Paciente paciente = pacientePortOutRead.findById(id)
                .orElseThrow(() -> PacienteNotFoundException.porId(id));
        paciente.setEstado(true);
        return pacientePortOutWrite.save(paciente);
    }

    @Override
    @Transactional (readOnly = true)
    public Paciente buscarByDni(String dni) {
        return pacientePortOutRead.buscarByDni(dni)
                .orElseThrow(() -> PacienteNotFoundException.porDni(dni));
    }

    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarByNombre(String nombre) {
        List<Paciente> paciente = pacientePortOutRead.buscarByNombre(nombre);
        if (paciente == null || paciente.isEmpty()) {
            throw PacienteNotFoundException.porNombre(nombre);
        }
        return paciente;
    }

    @Override
    @Transactional (readOnly = true)
    public List<Paciente> buscarPorObraSocialPaginado(String obraSocial, int limit, int offset) {
        List<Paciente> pacientes = pacientePortOutRead.buscarPorObraSocialPaginado(obraSocial, limit, offset);
        if (pacientes == null || pacientes.isEmpty()) {
            throw PacienteNotFoundException.porObraSocial(obraSocial);
        }
        return pacientes;
    }

}
