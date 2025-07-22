package com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "paciente")
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false, length = 100)
    private String name;
    @Column(name = "apellido", nullable = false, length = 100)
    private String lastName;
    @Column(name = "dni", nullable = false, unique = true, length = 20)
    private String dni;
    @Column(name = "obra_social", length = 100)
    private String healthInsuranceProvider;
    @Column(name = "email", length = 150)
    private String email;
    @Column(name = "telefono", length = 50)
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getHealthInsuranceProvider() {
        return healthInsuranceProvider;
    }

    public void setHealthInsuranceProvider(String healthInsuranceProvider) {
        this.healthInsuranceProvider = healthInsuranceProvider;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}