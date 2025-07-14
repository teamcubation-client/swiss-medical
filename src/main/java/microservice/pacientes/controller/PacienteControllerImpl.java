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

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PacienteResponseDTO> getByDniSP(@PathVariable String dni) {
        Paciente paciente = pacienteService.findByDniSP(dni);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.ok(pacienteResponseDTO);
    }


    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<PacienteResponseDTO> getByNombreSP(@PathVariable String nombre) {
        Paciente paciente = pacienteService.findByNombreSP(nombre);
        PacienteResponseDTO pacienteResponseDTO = PacienteResponseMapper.toDTO(paciente);
        return ResponseEntity.ok(pacienteResponseDTO);
    }


    @GetMapping("/obra_social")
    public ResponseEntity<List<PacienteResponseDTO>> getByObraSocialSP(
            @RequestParam(required = true) String obra_social,
            @RequestParam(required = true) int page,
            @RequestParam(required = true) int size
    ) {
        int limit = size;
        int offset = (page - 1) * size; // asumo que page empieza con 1
        List<Paciente> pacientes = pacienteService.findByObraSocialSP(obra_social, limit, offset);
        List<PacienteResponseDTO> pacientesResponseDTO = PacienteResponseMapper.toDTO(pacientes);
        return ResponseEntity.ok(pacientesResponseDTO);
    }

}
