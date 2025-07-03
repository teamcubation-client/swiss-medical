package microservice.pacientes.controller;

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
    public ResponseEntity<List<PacienteResponseDTO>> getPacientes(@RequestParam(required = false) String nombre) {
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
