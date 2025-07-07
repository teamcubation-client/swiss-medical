package com.practica.crud_pacientes.controlador;

import com.practica.crud_pacientes.dto.PacienteDto;
import com.practica.crud_pacientes.servicio.IPacienteServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PacienteControlador {

    private final IPacienteServicio pacienteServicio;

    public PacienteControlador(IPacienteServicio pacienteServicio) {
        this.pacienteServicio = pacienteServicio;
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDto>> getPacientes() {
        List<PacienteDto> pacienteObtenido = pacienteServicio.getPacientes();
        return ResponseEntity.ok(pacienteObtenido);
    }

    @GetMapping("/buscar-por-id/{idPaciente}")
    public ResponseEntity<PacienteDto> getPaciente(@PathVariable Integer idPaciente) {
        PacienteDto pacienteEncontrado = pacienteServicio.getPacientePorId(idPaciente);
        return  ResponseEntity.ok(pacienteEncontrado);
    }

    @GetMapping("/buscar-por-dni")
    public ResponseEntity<PacienteDto> getPacientePorDni(@RequestParam String dni){
        PacienteDto pacienteEncontrado = pacienteServicio.getPacientePorDni(dni);
        return ResponseEntity.ok(pacienteEncontrado);
    }

    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<PacienteDto>> getPacientePorNombre(@RequestParam String nombre){
        List<PacienteDto> pacientesEncontrados = pacienteServicio.getPacientePorNombre(nombre);
        return ResponseEntity.ok(pacientesEncontrados);
    }

    @PostMapping("/nuevo-paciente")
    public ResponseEntity<PacienteDto> agregarPaciente(@RequestBody PacienteDto pacienteNuevo){
        PacienteDto nuevoPaciente = pacienteServicio.addPaciente(pacienteNuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPaciente);
    }

    @PutMapping("actualizar/{idPaciente}")
    public ResponseEntity<PacienteDto> actualizarPaciente(@PathVariable Integer idPaciente, @RequestBody PacienteDto paciente) {
        PacienteDto pacienteActualizado = pacienteServicio.updatePaciente(idPaciente, paciente);
        return ResponseEntity.ok(pacienteActualizado);
    }

    @DeleteMapping("eliminar/{idPaciente}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Integer idPaciente) {
        pacienteServicio.deletePaciente(idPaciente);
        return ResponseEntity.noContent().build();
    }
}
