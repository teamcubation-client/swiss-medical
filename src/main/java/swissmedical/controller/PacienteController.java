package swissmedical.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import swissmedical.dto.PacienteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import swissmedical.model.Paciente;
import swissmedical.service.PacienteService;
import swissmedical.mapper.PacienteMapper;

import javax.validation.Valid;

/**
 * Controlador REST para la gestion de pacientes
 * Expone endpoints para listar, crear, consultar, actualizar y eliminar pacientes
 */
@RestController
@RequestMapping("/api/pacientes")

public class PacienteController implements IPacienteController {
    private final PacienteService pacienteService;
    private final PacienteMapper pacienteMapper;

    public PacienteController(PacienteService pacienteService, PacienteMapper pacienteMapper) {
        this.pacienteService = pacienteService;
        this.pacienteMapper = pacienteMapper;
    }

    /**
     * Obtiene la lista de todos los pacientes registrados
     * @return lista de PacienteDTO
     */
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarPacientes();
        List<PacienteDTO> dtos = new ArrayList<>();
        for (Paciente paciente : pacientes) {
            dtos.add(pacienteMapper.toDTO(paciente));
        }
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * Crea un nuevo paciente a partir de un DTO
     * @param pacienteDTO datos del paciente a crear
     * @return PacienteDTO creado
     */

    @PostMapping
    public ResponseEntity<PacienteDTO> crearPaciente(@Valid @RequestBody PacienteDTO pacienteDTO) {
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
        Paciente creado = pacienteService.crearPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteMapper.toDTO(creado));
    }

    /**
     * Obtiene un paciente por su identificador unico
     * @param id identificador del paciente
     * @return PacienteDTO encontrado o null si no existe
     */

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> obtenerPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteService.obtenerPacientePorId(id);
        if (paciente != null) {
            return ResponseEntity.ok(pacienteMapper.toDTO(paciente));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Elimina un paciente por su identificador unico
     * @param id identificador del paciente a eliminar
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca un paciente por su DNI
     * @param dni Documento Nacional de Identidad
     * @return PacienteDTO encontrado o null si no existe
     */

    @GetMapping("/buscar/dni")
    public ResponseEntity<PacienteDTO> buscarPorDni(@RequestParam String dni) {
        Paciente paciente = pacienteService.buscarPorDni(dni);
        if (paciente != null) {
            return ResponseEntity.ok(pacienteMapper.toDTO(paciente));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca pacientes cuyo nombre contenga el string especificado
     * @param nombre parte o nombre completo a buscar
     * @return lista de PacienteDTO que coinciden con el parametro
     */

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<PacienteDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<Paciente> pacientes = pacienteService.buscarPorNombreParcial(nombre);
        List<PacienteDTO> pacienteDTOs = new ArrayList<>();
        for (Paciente paciente : pacientes) {
            pacienteDTOs.add(pacienteMapper.toDTO(paciente));
        }
        return ResponseEntity.ok(pacienteDTOs);
    }



    /**
     * Actualiza los datos de un paciente existente
     * @param id identificador del paciente a actualizar
     * @param pacienteDTO datos nuevos del paciente
     * @return PacienteDTO actualizado o null si no existe
     */

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO) {
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
        Paciente actualizado = pacienteService.actualizarPaciente(id, paciente);
        if (actualizado != null) {
            return ResponseEntity.ok(pacienteMapper.toDTO(actualizado));
        } else {
            return ResponseEntity.notFound().build();

        }
    }

    @PatchMapping("/{id}/activar")
    @Override
    public ResponseEntity<PacienteDTO> activarPaciente(@PathVariable Long id) {
        Paciente activado = pacienteService.activarPaciente(id);
        return ResponseEntity.ok(pacienteMapper.toDTO(activado));
    }

    @PatchMapping("/{id}/desactivar")
    @Override
    public ResponseEntity<PacienteDTO> desactivarPaciente(@PathVariable Long id) {
        Paciente desactivado = pacienteService.desactivarPaciente(id);
        return ResponseEntity.ok(pacienteMapper.toDTO(desactivado));
    }

    @GetMapping("/activos")
    @Override
    public ResponseEntity<List<PacienteDTO>> listarPacientesActivos() {
        List<Paciente> pacientes = pacienteService.listarPacientes();
        List<PacienteDTO> activos = new ArrayList<>();
        for (Paciente paciente : pacientes) {
            if (paciente.isEstado()) {
                activos.add(pacienteMapper.toDTO(paciente));
            }
        }
        return ResponseEntity.ok(activos);
    }

    @GetMapping("/inactivos")
    @Override
    public ResponseEntity<List<PacienteDTO>> listarPacientesInactivos() {
        List<Paciente> pacientes = pacienteService.listarPacientes();
        List<PacienteDTO> inactivos = new ArrayList<>();
        for (Paciente paciente : pacientes) {
            if (!paciente.isEstado()) {
                inactivos.add(pacienteMapper.toDTO(paciente));
            }
        }
        return ResponseEntity.ok(inactivos);
    }
}
