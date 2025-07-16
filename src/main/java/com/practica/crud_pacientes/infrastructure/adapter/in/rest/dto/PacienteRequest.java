package com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class PacienteRequest {
    @NotEmpty(message = "Debe ingresar un nombre.")
    @Size(min = 3, max = 25)
    private String nombre;

    @NotEmpty(message = "Debe ingresar un apellido.")
    @Size(min = 3, max = 25)
    private String apellido;

    @NotEmpty(message = "Debe ingresar un DNI.")
    @Size(min = 8, max = 8, message = "Ingrese un DNI valido")
    @Pattern(regexp = "\\d+", message = "El DNI debe contener solo números.")
    private String dni;

    @NotEmpty(message = "Debe ingresar una obra social.")
    @Size(min = 2, max = 20)
    private String obraSocial;

    @NotEmpty(message = "Debe ingresar un email.")
    @Email(message = "Ingrese un formato de mail valido")
    private String email;

    @NotEmpty(message = "Debe ingresar un telefono.")
    @Pattern(regexp = "\\d{6,15}", message = "El telefono debe contener solo números.")
    private String telefono;

    @NotEmpty(message = "Debe ingresar un domicilio.")
    private String domicilio;

    @NotNull(message = "Debe ingresar la fecha de nacimiento.")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;

    @NotEmpty(message = "Debe ingresar el estado civil.")
    @Size(min = 3)
    private String estadoCivil;

    public PacienteRequest() {
    }

    public PacienteRequest(String nombre, String apellido, String dni, String obraSocial, String email, String telefono, String domicilio, LocalDate fechaNacimiento, String estadoCivil) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.obraSocial = obraSocial;
        this.email = email;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.fechaNacimiento = fechaNacimiento;
        this.estadoCivil = estadoCivil;
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

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
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

    public String getDomicilio() {
        return domicilio;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }
}
