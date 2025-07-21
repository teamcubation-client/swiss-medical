package microservice.pacientes.infrastructure.adapter.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static microservice.pacientes.infrastructure.adapter.in.controller.ApiResponseConst.*;


public interface PacienteApi {
    @Operation(summary = "Listar pacientes", description = "Lista todos los pacientes en el sistema", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_LIST),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @GetMapping
    ResponseEntity<List<PacienteDTO>> listarPacientes();


    @Operation(summary = "Crear un nuevo paciente", description = "Registra un nuevo paciente en el sistema", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_CREATED, description = DESC_CREATED),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @PostMapping
    ResponseEntity<PacienteDTO> crearPaciente(@RequestBody PacienteDTO pacienteDTO);


    @Operation(summary = "Buscar paciente por ID", description = "Busca al paciente con por el campo ID correspondiente", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_OK),
        @ApiResponse(responseCode = CODE_NOT_FOUND, description = DESC_NOT_FOUND_ID),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @GetMapping("/{id}")
    ResponseEntity<PacienteDTO> obtenerPaciente(@PathVariable Long id);


    @Operation(summary = "Eliminar paciente por ID", description = "Elimina al paciente con por el campo ID correspondiente", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_NO_CONTENT, description = DESC_DELETED),
        @ApiResponse(responseCode = CODE_NOT_FOUND, description = DESC_NOT_FOUND_ID),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminarPaciente(@PathVariable Long id);


    @Operation(summary = "Buscar paciente por DNI", description = "Busca al paciente con por el campo DNI correspondiente", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_OK),
        @ApiResponse(responseCode = CODE_NOT_FOUND, description = DESC_NOT_FOUND_DNI),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @GetMapping("/buscar/dni")
    ResponseEntity<PacienteDTO> buscarPorDni(@RequestParam String dni);


    @Operation(summary = "Buscar paciente por Nombre", description = "Busca al paciente con por el campo Nombre parcial o completo", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_OK),
        @ApiResponse(responseCode = CODE_NOT_FOUND, description = DESC_NOT_FOUND_NAME),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @GetMapping("/buscar/nombre")
    ResponseEntity<List<PacienteDTO>> buscarPorNombre(@RequestParam String nombre);


    @Operation(summary = "Actualizar datos del paciente", description = "Actualiza los datos del paciente con los campos ingresados", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_UPDATED),
        @ApiResponse(responseCode = CODE_NOT_FOUND, description = DESC_NOT_FOUND_ID),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @PutMapping("/{id}")
    ResponseEntity<PacienteDTO> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO);


    @Operation(summary = "Activar paciente", description = "Activa el paciente con el ID dado", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_ACTIVATED),
        @ApiResponse(responseCode = CODE_NOT_FOUND, description = DESC_NOT_FOUND_ID),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @PatchMapping("/{id}/activar")
    ResponseEntity<PacienteDTO> activarPaciente(@PathVariable Long id);


    @Operation(summary = "Desactivar paciente", description = "Desactiva el paciente con el ID dado", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_DEACTIVATED),
        @ApiResponse(responseCode = CODE_NOT_FOUND, description = DESC_NOT_FOUND_ID),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @PatchMapping("/{id}/desactivar")
    ResponseEntity<PacienteDTO> desactivarPaciente(@PathVariable Long id);


    @Operation(summary = "Listar pacientes activos", description = "Lista todos los pacientes con estado activo", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_LIST_ACTIVATED),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description =  DESC_INTERNAL_ERROR)
    })
    @GetMapping("/activos")
    ResponseEntity<List<PacienteDTO>> listarPacientesActivos();


    @Operation(summary = "Listar pacientes inactivos", description = "Lista todos los pacientes con estado inactivo", tags = {"Paciente"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_LIST_DEACTIVATED),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description =  DESC_INTERNAL_ERROR)
    })
    @GetMapping("/inactivos")
    ResponseEntity<List<PacienteDTO>> listarPacientesInactivos();

    @Operation(summary = "Buscar paciente por DNI (SP)", description = "Busca un paciente por DNI usando stored procedure", tags = {"Stored Procedure"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_OK),
        @ApiResponse(responseCode = CODE_NOT_FOUND, description = DESC_NOT_FOUND_DNI),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @GetMapping("/sp/buscar/dni/{dni}")
    ResponseEntity<PacienteDTO> buscarByDni(@PathVariable String dni);

    @Operation(summary = "Buscar pacientes por Nombre (SP)", description = "Busca pacientes por nombre usando stored procedure", tags = {"Stored Procedure"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_OK),
        @ApiResponse(responseCode = CODE_NOT_FOUND, description = DESC_NOT_FOUND_NAME),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @GetMapping("/sp/buscar/nombre/{nombre}")
    ResponseEntity<List<PacienteDTO>> buscarByNombre(@PathVariable String nombre);

    @Operation(summary = "Buscar pacientes por Obra Social (SP)", description = "Busca pacientes por obra social con paginacion usando stored procedure", tags = {"Stored Procedure"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_OK, description = DESC_OK),
        @ApiResponse(responseCode = CODE_NOT_FOUND, description = DESC_NOT_FOUND_OBRASOCIAL),
        @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = DESC_INTERNAL_ERROR)
    })
    @GetMapping("/sp/buscar/obra-social")
    ResponseEntity<List<PacienteDTO>> buscarPorObraSocialPaginado(@RequestParam String obraSocial, @RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int offset);
} 