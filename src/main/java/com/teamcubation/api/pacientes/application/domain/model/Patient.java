package com.teamcubation.api.pacientes.application.domain.model;

public class Patient {

    private Long id;
    private String name;
    private String lastName;
    private String dni;
    private String healthInsuranceProvider;
    private String email;
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
