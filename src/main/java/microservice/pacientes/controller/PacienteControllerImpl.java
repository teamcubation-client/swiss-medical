package microservice.pacientes.controller;

import microservice.pacientes.controller.dto.PacienteRequestDTO;
import microservice.pacientes.controller.dto.PacienteResponseDTO;
import microservice.pacientes.controller.dto.PacienteUpdateDTO;
import microservice.pacientes.model.Paciente;
import microservice.pacientes.service.PacienteService;
import microservice.pacientes.util.PacienteResponseMapper;
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
        List<Paciente> pacientes;
        if(nombre != null){
            pacientes = pacienteService.findByNombreContainingIgnoreCase(nombre);
        } else {
            pacientes = pacienteService.getAll();
        }
        List<PacienteResponseDTO> pacientesResponseDTO = PacienteResponseMapper.toDTO(pacientes);
        return ResponseEntity.ok(pacientesResponseDTO);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> getByDni(@PathVariable String dni) {
        Paciente paciente = pacienteService.getByDni(dni);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> create(@RequestBody PacienteRequestDTO pacienteRequestDTO) {
        Paciente paciente = pacienteService.create(pacienteRequestDTO);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteResponseDTO);
    }

    @PutMapping("/{dni}")
    public ResponseEntity<PacienteResponseDTO> update(@PathVariable String dni, @RequestBody PacienteUpdateDTO pacienteUpdateDTO) {
        Paciente paciente = pacienteService.update(dni, pacienteUpdateDTO);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.ok(pacienteResponseDTO);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String dni) {
        pacienteService.delete(dni);
        return ResponseEntity.noContent().build();
    }

}
