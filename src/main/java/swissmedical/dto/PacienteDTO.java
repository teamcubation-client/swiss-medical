package swissmedical.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

/**
 * Data Transfer Object para transferir informacion de pacientes entre capas de la aplicacion
 * Incluye datos personales, de contacto y la obra social del paciente
 */
public class PacienteDTO {
    /** Identificador unico del paciente */
    private Long id;
    /** Nombre del paciente */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    /** Apellido del paciente */
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    /** Documento Nacional de Identidad del paciente */
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;
    /** Obra social a la que pertenece el paciente */
    @NotBlank(message = "La obra social es obligatoria")
    private String obraSocial;
    /** Correo electronico del paciente */
    @NotBlank(message = "El correo electrónico es obligatorio")
    private String email;
    /** Telefono de contacto del paciente */
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
    /** Tipo de plan de la obra social */
    @NotBlank(message = "El tipo de plan de obra social es obligatorio")
    private String tipoPlanObraSocial;
    /** Fecha de alta del paciente en el sistema */
    @PastOrPresent(message = "La fecha de alta no puede ser futura")
    private java.time.LocalDate fechaAlta;
    /** Estado del paciente (true=activo, false=inactivo) */
    private boolean estado;

    /**
     * Constructor con todos los campos del DTO.
     * @param id Identificador unico
     * @param nombre Nombre del paciente
     * @param apellido Apellido del paciente
     * @param dni Documento Nacional de Identidad
     * @param obraSocial Obra social
     * @param email Correo electronico
     * @param telefono Telefono de contacto
     * @param tipoPlanObraSocial Tipo de plan de la obra social
     * @param fechaAlta Fecha de alta del paciente
     * @param estado Estado del paciente (true=activo, false=inactivo)
     */
    public PacienteDTO(Long id, String nombre, String apellido, String dni, String obraSocial, String email, String telefono, String tipoPlanObraSocial, java.time.LocalDate fechaAlta, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.obraSocial = obraSocial;
        this.email = email;
        this.telefono = telefono;
        this.tipoPlanObraSocial = tipoPlanObraSocial;
        this.fechaAlta = fechaAlta;
        this.estado = estado;
    }

    /**
     * Obtiene el identificador unico del paciente
     * @return id del paciente
     */
    public Long getId() { return id; }
    /**
     * Establece el identificador unico del paciente
     * @param id identificador unico
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Obtiene el nombre del paciente
     * @return nombre del paciente
     */
    public String getNombre() { return nombre; }
    /**
     * Establece el nombre del paciente
     * @param nombre nombre del paciente
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
    /**
     * Obtiene el apellido del paciente
     * @return apellido del paciente
     */
    public String getApellido() { return apellido; }
    /**
     * Establece el apellido del paciente
     * @param apellido apellido del paciente
     */
    public void setApellido(String apellido) { this.apellido = apellido; }
    /**
     * Obtiene el DNI del paciente
     * @return DNI del paciente
     */
    public String getDni() { return dni; }
    /**
     * Establece el DNI del paciente
     * @param dni Documento Nacional de Identidad
     */
    public void setDni(String dni) { this.dni = dni; }
    /**
     * Obtiene la obra social del paciente
     * @return obra social
     */
    public String getObraSocial() { return obraSocial; }
    /**
     * Establece la obra social del paciente
     * @param obraSocial obra social
     */
    public void setObraSocial(String obraSocial) { this.obraSocial = obraSocial; }
    /**
     * Obtiene el correo electronico del paciente
     * @return correo electronico
     */
    public String getEmail() { return email; }
    /**
     * Establece el correo electronico del paciente
     * @param email correo electronico
     */
    public void setEmail(String email) { this.email = email; }
    /**
     * Obtiene el telefono de contacto del paciente
     * @return telefono de contacto
     */
    public String getTelefono() { return telefono; }
    /**
     * Establece el telefono de contacto del paciente
     * @param telefono telefono de contacto
     */
    public void setTelefono(String telefono) { this.telefono = telefono; }
    /**
     * Obtiene el tipo de plan de la obra social
     * @return tipo de plan de la obra social
     */
    public String getTipoPlanObraSocial() {
        return tipoPlanObraSocial;
    }
    /**
     * Establece el tipo de plan de la obra social
     * @param tipoPlanObraSocial tipo de plan de la obra social
     */
    public void setTipoPlanObraSocial(String tipoPlanObraSocial) {
        this.tipoPlanObraSocial = tipoPlanObraSocial;
    }
    /**
     * Obtiene la fecha de alta del paciente en el sistema
     * @return fecha de alta del paciente
     */
    public java.time.LocalDate getFechaAlta() {
        return fechaAlta;
    }
    /**
     * Establece la fecha de alta del paciente en el sistema
     * @param fechaAlta fecha de alta del paciente
     */
    public void setFechaAlta(java.time.LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    /**
     * Obtiene el estado del paciente
     * @return true si activo, false si inactivo
     */
    public boolean isEstado() {
        return estado;
    }
    /**
     * Establece el estado del paciente
     * @param estado true si activo, false si inactivo
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
} 