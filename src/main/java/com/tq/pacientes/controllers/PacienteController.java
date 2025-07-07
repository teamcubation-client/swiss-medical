package com.tq.pacientes.controllers;

import com.tq.pacientes.documentation.IPacienteController;
import com.tq.pacientes.dtos.ActualizarPacienteDTO;
import com.tq.pacientes.dtos.PacienteDTO;
import com.tq.pacientes.models.Paciente;
import com.tq.pacientes.services.PacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController implements IPacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> crear(@RequestBody Paciente paciente) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pacienteService.crearPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> buscar(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String nombre
    ) {
        if (dni != null) {
            return ResponseEntity.ok(List.of(pacienteService.buscarPorDni(dni)));
        } else if (nombre != null) {
            return ResponseEntity.ok(pacienteService.buscarPorNombre(nombre));
        } else {
            return ResponseEntity.ok(pacienteService.listarPacientes());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscarPorId(@PathVariable Long id) {
        PacienteDTO dto = pacienteService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PacienteDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ActualizarPacienteDTO dto
    ) {
        PacienteDTO actualizado = pacienteService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
