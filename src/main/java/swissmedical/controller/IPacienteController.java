package swissmedical.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import swissmedical.dto.PacienteDTO;
import java.util.List;

public interface IPacienteController {
    @Operation(summary = "Listar pacientes", description = "Lista todos los pacientes en el sistema", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pacientes listados exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay pacientes dados de alta"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping
    List<PacienteDTO> listarPacientes();

    @Operation(summary = "Crear un nuevo paciente", description = "Registra un nuevo paciente en el sistema", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @PostMapping
    PacienteDTO crearPaciente(@RequestBody PacienteDTO pacienteDTO);

    @Operation(summary = "Buscar paciente por ID", description = "Busca al paciente con por el campo ID correspondiente", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "400", description = "Formato invalido de ID"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping("/{id}")
    PacienteDTO obtenerPaciente(@PathVariable Long id);

    @Operation(summary = "Eliminar paciente por ID", description = "Elimina al paciente con por el campo ID correspondiente", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "400", description = "Formato invalido de ID"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @DeleteMapping("/{id}")
    void eliminarPaciente(@PathVariable Long id);

    @Operation(summary = "Buscar paciente por DNI", description = "Busca al paciente con por el campo DNI correspondiente", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el DNI"),
        @ApiResponse(responseCode = "400", description = "Formato invalido de DNI"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping("/buscar/dni")
    PacienteDTO buscarPorDni(@RequestParam String dni);

    @Operation(summary = "Buscar paciente por Nombre", description = "Busca al paciente con por el campo Nombre parcial o completo", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el nombre"),
        @ApiResponse(responseCode = "400", description = "Formato invalido de nombre"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping("/buscar/nombre")
    List<PacienteDTO> buscarPorNombre(@RequestParam String nombre);

    @Operation(summary = "Actualizar datos del paciente", description = "Actualiza los datos del paciente con los campos ingresados", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "400", description = "Cuerpo invalido o ID invalido"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @PutMapping("/{id}")
    PacienteDTO actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO);

    @Operation(summary = "Invertir estado del paciente", description = "Invierte el estado (activo/inactivo) del paciente con el ID dado", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado del paciente invertido exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "400", description = "ID invalido"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @PatchMapping("/{id}/cambiar-estado")
    PacienteDTO cambiarEstado(@PathVariable Long id);

    @Operation(summary = "Activar paciente", description = "Activa el paciente con el ID dado", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente activado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "400", description = "ID invalido"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @PatchMapping("/{id}/activar")
    PacienteDTO activarPaciente(@PathVariable Long id);

    @Operation(summary = "Inactivar paciente", description = "Inactiva el paciente con el ID dado", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente inactivado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No existe el paciente con el ID"),
        @ApiResponse(responseCode = "400", description = "ID invalido"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @PatchMapping("/{id}/inactivar")
    PacienteDTO inactivarPaciente(@PathVariable Long id);

    @Operation(summary = "Listar pacientes activos", description = "Lista todos los pacientes con estado activo", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pacientes activos listados exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay pacientes activos"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping("/activos")
    List<PacienteDTO> listarPacientesActivos();

    @Operation(summary = "Listar pacientes inactivos", description = "Lista todos los pacientes con estado inactivo", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pacientes inactivos listados exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay pacientes inactivos"),
        @ApiResponse(responseCode = "500", description = "Error del sistema")
    })
    @GetMapping("/inactivos")
    List<PacienteDTO> listarPacientesInactivos();
} 