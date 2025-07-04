package com.practica.crud_pacientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@NoArgsConstructor
public class PacienteDto {

    @NotEmpty(message = "Debe ingresar un nombre.")
    @Size(min = 3, max = 25)
    private String nombre;
    @NotEmpty(message = "Debe ingresar un apellido.")
    @Size(min = 3, max = 25)
    private String apellido;
    @NotEmpty(message = "Debe ingresar un DNI.")
    @Size(min = 8, max = 8, message = "Ingrese un DNI valido")
    private String dni;
    @NotEmpty(message = "Debe ingresar una obra social.")
    private String obraSocial;
    @NotEmpty(message = "Debe ingresar un email.")
    @Email
    private String email;
    @NotEmpty(message = "Debe ingresar un telefono.")
    private String telefono;
    @NotEmpty(message = "Debe ingresar un domicilio.")
    private String domicilio;
    @NotNull(message = "Debe ingresar la fecha de nacimiento.")
    private LocalDate fechaNacimiento;
    @NotEmpty(message = "Debe ingresar el estado civil.")
    private String estadoCivil;

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }
}
