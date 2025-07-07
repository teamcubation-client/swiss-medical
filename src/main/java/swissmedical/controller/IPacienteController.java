package swissmedical.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swissmedical.dto.PacienteDTO;
import java.util.List;

public interface IPacienteController {
    @Operation(summary = "Listar pacientes", description = "Lista todos los pacientes en el sistema", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pacientes listados exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping
    ResponseEntity<List<PacienteDTO>> listarPacientes();


    @Operation(summary = "Crear un nuevo paciente", description = "Registra un nuevo paciente en el sistema", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @PostMapping
    ResponseEntity<PacienteDTO> crearPaciente(@RequestBody PacienteDTO pacienteDTO);


    @Operation(summary = "Buscar paciente por ID", description = "Busca al paciente con por el campo ID correspondiente", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping("/{id}")
    ResponseEntity<PacienteDTO> obtenerPaciente(@PathVariable Long id);


    @Operation(summary = "Eliminar paciente por ID", description = "Elimina al paciente con por el campo ID correspondiente", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Paciente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminarPaciente(@PathVariable Long id);


    @Operation(summary = "Buscar paciente por DNI", description = "Busca al paciente con por el campo DNI correspondiente", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el DNI"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping("/buscar/dni")
    ResponseEntity<PacienteDTO> buscarPorDni(@RequestParam String dni);


    @Operation(summary = "Buscar paciente por Nombre", description = "Busca al paciente con por el campo Nombre parcial o completo", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el nombre"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping("/buscar/nombre")
    ResponseEntity<List<PacienteDTO>> buscarPorNombre(@RequestParam String nombre);


    @Operation(summary = "Actualizar datos del paciente", description = "Actualiza los datos del paciente con los campos ingresados", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @PutMapping("/{id}")
    ResponseEntity<PacienteDTO> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO);


    @Operation(summary = "Activar paciente", description = "Activa el paciente con el ID dado", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente activado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @PatchMapping("/{id}/activar")
    ResponseEntity<PacienteDTO> activarPaciente(@PathVariable Long id);


    @Operation(summary = "Desactivar paciente", description = "Desactiva el paciente con el ID dado", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente Desactivado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @PatchMapping("/{id}/desactivar")
    ResponseEntity<PacienteDTO> desactivarPaciente(@PathVariable Long id);


    @Operation(summary = "Listar pacientes activos", description = "Lista todos los pacientes con estado activo", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pacientes activos"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping("/activos")
    ResponseEntity<List<PacienteDTO>> listarPacientesActivos();


    @Operation(summary = "Listar pacientes inactivos", description = "Lista todos los pacientes con estado inactivo", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pacientes inactivos"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping("/inactivos")
    ResponseEntity<List<PacienteDTO>> listarPacientesInactivos();
} 