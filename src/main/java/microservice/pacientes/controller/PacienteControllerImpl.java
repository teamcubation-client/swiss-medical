package microservice.pacientes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import microservice.pacientes.dto.PacienteRequestDTO;
import microservice.pacientes.dto.PacienteResponseDTO;
import microservice.pacientes.dto.PacienteUpdateDTO;
import microservice.pacientes.service.PacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteControllerImpl implements PacienteController {

    private final PacienteService pacienteService;

    public PacienteControllerImpl(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> getAll(@RequestParam(required = false) String nombre) {
        List<PacienteResponseDTO> pacientes;
        if(nombre != null){
            pacientes = pacienteService.findByNombreContainingIgnoreCase(nombre);
        } else {
            pacientes = pacienteService.getPacientes();
        }
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> getByDni(@PathVariable String dni) {
        PacienteResponseDTO paciente = pacienteService.getPacienteByDni(dni);
        return ResponseEntity.ok(paciente);
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> create(@RequestBody PacienteRequestDTO pacienteRequestDTO) {
        PacienteResponseDTO paciente = pacienteService.createPaciente(pacienteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
    }

    @PutMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> update(@PathVariable String dni, @RequestBody PacienteUpdateDTO pacienteUpdateDTO) {
        PacienteResponseDTO pacienteResponseDTO = pacienteService.updatePaciente(dni, pacienteUpdateDTO);
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String dni) {
        pacienteService.deletePaciente(dni);
        return ResponseEntity.noContent().build();
    }

}
