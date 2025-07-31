package com.teamcubation.api.pacientes.shared;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
/**
 * Se puede usar para utilizar paciente con campos obligatorios por defecto o crear nuevos pacientes
 */
public class TestPatientBuilder {
    private Long id = 1L;
    private String name = "Roberto";
    private String lastName = "Gonzáles";
    private String dni = "35784627";
    private String insurance = null;
    private String email = null;
    private String phone = null;

    public TestPatientBuilder withId(Long id) { this.id = id; return this; }
    public TestPatientBuilder withName(String name) { this.name = name; return this; }
    public TestPatientBuilder withLastName(String lastName) { this.lastName = lastName; return this; }
    public TestPatientBuilder withDni(String dni) { this.dni = dni; return this; }
    public TestPatientBuilder withInsurance(String insurance) { this.insurance = insurance; return this; }
    public TestPatientBuilder withEmail(String email) { this.email = email; return this; }
    public TestPatientBuilder withPhone(String phone) { this.phone = phone; return this; }

    public Patient build() {
        Patient p = new Patient();
        p.setId(id);
        p.setName(name);
        p.setLastName(lastName);
        p.setDni(dni);
        p.setHealthInsuranceProvider(insurance);
        p.setEmail(email);
        p.setPhoneNumber(phone);
        return p;
    }
}
