package com.practica.crud_pacientes.controlador;

import com.practica.crud_pacientes.dto.PacienteDto;
import com.practica.crud_pacientes.excepciones.PacienteNoEncontradoException;
import com.practica.crud_pacientes.servicio.IPacienteServicio;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
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
    @ResponseStatus
    public ResponseEntity<List<PacienteDto>> getPacientes() {
        try{
            List<PacienteDto> pacienteObtenido = pacienteServicio.getPacientes();
            return new ResponseEntity<>(pacienteObtenido, HttpStatus.OK);
        }catch(RuntimeException e){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/buscar-por-id/{idPaciente}")
    @ResponseStatus
    public ResponseEntity<?> getPaciente(@PathVariable Integer idPaciente) {
        try{
            PacienteDto pacienteEncontrado = pacienteServicio.getPacientePorId(idPaciente);
            return new ResponseEntity<>(pacienteEncontrado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscar-por-dni")
    @ResponseStatus
    public ResponseEntity<?> getPacientePorDni(@RequestParam String dni){
        try{
            PacienteDto pacienteEncontrado = pacienteServicio.getPacientePorDni(dni);
            return new ResponseEntity<>(pacienteEncontrado, HttpStatus.OK);
        }catch(RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscar-por-nombre")
    @ResponseStatus
    public ResponseEntity<List<PacienteDto>> getPacientePorNombre(@RequestParam String nombre){
        try{
            List<PacienteDto> pacientesEncontrados = pacienteServicio.getPacientePorNombre(nombre);
            return new ResponseEntity<>(pacientesEncontrados, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/nuevo-paciente")
    @ResponseStatus
    public ResponseEntity<?> agregarPaciente(@RequestBody PacienteDto pacienteNuevo){
        try{
            PacienteDto nuevoPaciente = pacienteServicio.addPaciente(pacienteNuevo);
            return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("actualizar/{idPaciente}")
    @ResponseStatus
    public ResponseEntity<?> actualizarPaciente(@PathVariable Integer idPaciente, @RequestBody PacienteDto paciente) throws PacienteNoEncontradoException {
        try{
            PacienteDto pacienteActualizado = pacienteServicio.updatePaciente(idPaciente, paciente);
            return new ResponseEntity<>(pacienteActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("eliminar/{idPaciente}")
    public ResponseEntity<?> eliminarPaciente(@PathVariable Integer idPaciente) throws PacienteNoEncontradoException {
        try{
            pacienteServicio.deletePaciente(idPaciente);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
