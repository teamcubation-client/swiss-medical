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
import org.springframework.beans.factory.annotation.Autowired;
import swissmedical.mapper.PacienteMapper;

import javax.validation.Valid;

/**
 * Controlador REST para la gestion de pacientes
 * Expone endpoints para listar, crear, consultar, actualizar y eliminar pacientes
 */
@RestController
@RequestMapping("/api/pacientes")

public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteMapper pacienteMapper;

    /**
     * Obtiene la lista de todos los pacientes registrados
     * @return lista de PacienteDTO
     */
    @Operation(
            summary = "Listar pacientes",
            description = "Lista todos los pacientes en el sistema",
            tags = {"Paciente"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes listados exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
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
    @Operation(
            summary = "Crear un nuevo paciente",
            description = "Registra un nuevo paciente en el sistema",
            tags = {"Paciente"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
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
    @Operation(
            summary = "Buscar paciente por ID",
            description = "Busca al paciente con por el campo ID correspondiente",
            tags = {"Paciente"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
            @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
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
    @Operation(
            summary = "Eliminar paciente por ID",
            description = "Elimina al paciente con por el campo ID correspondiente",
            tags = {"Paciente"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
            @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
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
    @Operation(
            summary = "Buscar paciente por DNI",
            description = "Busca al paciente con por el campo DNI correspondiente",
            tags = {"Paciente"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No existe el paciente con el DNI"),
            @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
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
    @Operation(
            summary = "Buscar paciente por Nombre",
            description = "Busca al paciente con por el campo Nombre parcial o completo",
            tags = {"Paciente"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No existe el paciente con el nombre"),
            @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
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
    @Operation(
            summary = "Actualizar datos del paciente",
            description = "Actualiza los datos del paciente con los campos ingresados",
            tags = {"Paciente"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
            @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
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
} 