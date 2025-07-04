package microservice.pacientes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import microservice.pacientes.dto.PacienteRequestDTO;
import microservice.pacientes.dto.PacienteResponseDTO;
import microservice.pacientes.dto.PacienteUpdateDTO;
import microservice.pacientes.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    @Operation(
            summary = "Obtener todos los pacientes",
            description = "Devuelve los datos de todos los pacientes registrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados. Incluye lista vacía."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<PacienteResponseDTO>> getPacientes(
            @Parameter(
                    description = "Nombre parcial o completo para filtrar los pacientes. Opcional.",
                    required = false
            )
            @RequestParam(required = false) String nombre
    ) {
        if(nombre != null && !nombre.isEmpty()){
            List<PacienteResponseDTO> pacientes = pacienteService.findByNombreContainingIgnoreCase(nombre);
            return ResponseEntity.ok(pacientes);
        } else {
            List<PacienteResponseDTO> pacientes = pacienteService.getPacientes();
            return ResponseEntity.ok(pacientes);
        }
    }

    @GetMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> getPacienteByDni(@PathVariable String dni) {
        PacienteResponseDTO paciente = pacienteService.getPacienteByDni(dni);
        return ResponseEntity.ok(paciente);
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> createPaciente(@RequestBody PacienteRequestDTO pacienteRequestDTO) {
        PacienteResponseDTO paciente = pacienteService.createPaciente(pacienteRequestDTO);
        return ResponseEntity.ok(paciente);
    }

    @PutMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> updatePaciente(@PathVariable String dni, @RequestBody PacienteUpdateDTO pacienteUpdateDTO) {
        PacienteResponseDTO paciente = pacienteService.updatePaciente(dni, pacienteUpdateDTO);
        return ResponseEntity.ok(paciente);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deletePaciente(@PathVariable String dni) {
        pacienteService.deletePaciente(dni);
        return ResponseEntity.noContent().build();
    }

}
