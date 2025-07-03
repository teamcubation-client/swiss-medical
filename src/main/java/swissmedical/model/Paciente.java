package swissmedical.model;

import javax.persistence.*;

/**
 * Entidad que representa a un paciente
 * Contiene informacion personal y de contacto, ademas de la obra social al que pertenece el paciente
 */
@Entity
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identificador unico del paciente */
    private Long id;
    /** Nombre del paciente */
    private String nombre;
    /** Apellido del paciente */
    private String apellido;
    /** Documento Nacional de Identidad del paciente */
    private String dni;
    /** Obra social a la que pertenece el paciente */
    private String obraSocial;
    /** Correo electronico del paciente */
    private String email;
    /** Telefono de contacto del paciente */
    private String telefono;

    /**
     * Constructor vacio requerido por JPA
     */
    public Paciente() {}

    /**
     * Constructor con todos los campos.
     * @param id Identificador unico
     * @param nombre Nombre del paciente
     * @param apellido Apellido del paciente
     * @param dni Documento Nacional de Identidad
     * @param obraSocial Obra social
     * @param email Correo electronico
     * @param telefono Telefono de contacto
     */
    public Paciente(Long id, String nombre, String apellido, String dni, String obraSocial, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.obraSocial = obraSocial;
        this.email = email;
        this.telefono = telefono;
    }

    /**
     * Obtiene el identificador unico del paciente
     * @return id del paciente
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador unico del paciente
     * @param id identificador único
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del paciente
     * @return nombre del paciente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del paciente
     * @param nombre nombre del paciente
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del paciente
     * @return apellido del paciente
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del paciente
     * @param apellido apellido del paciente
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el DNI del paciente
     * @return DNI del paciente
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del paciente
     * @param dni Documento Nacional de Identidad
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene la obra social del paciente
     * @return obra social
     */
    public String getObraSocial() {
        return obraSocial;
    }

    /**
     * Establece la obra social del paciente
     * @param obraSocial obra social
     */
    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    /**
     * Obtiene el correo electronico del paciente
     * @return correo electronico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electronico del paciente
     * @param email correo electronico
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el telefono de contacto del paciente
     * @return telefono de contacto
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el telefono de contacto del paciente
     * @param telefono telefono de contacto
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
} 