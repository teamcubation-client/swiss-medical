package com.practica.crud_pacientes.utils;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteRequest;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteResponse;

import java.time.LocalDate;

public class PacienteTestFactory {
    public static PacienteRequest buildRequest() {
        PacienteRequest request = new PacienteRequest();
        request.setNombre("Jane");
        request.setApellido("Doe");
        request.setDni("12121212");
        request.setObraSocial("Swiss Medical");
        request.setEmail("jane@gmail.com");
        request.setTelefono("1122334455");
        request.setDomicilio("Fake Street 123");
        request.setFechaNacimiento(LocalDate.of(2000, 6, 20));
        request.setEstadoCivil("Soltera");
        return request;
    }

    public static PacienteResponse buildResponse() {
        PacienteResponse response = new PacienteResponse();
        response.setNombre("Jane");
        response.setApellido("Doe");
        response.setDni("12121212");
        response.setObraSocial("Swiss Medical");
        response.setEmail("jane@gmail.com");
        response.setTelefono("1122334455");
        response.setDomicilio("Fake Street 123");
        response.setFechaNacimiento(LocalDate.of(2000, 6, 20));
        response.setEstadoCivil("Soltera");
        return response;
    }
    public static Paciente buildDomain() {
        Paciente pacienteDomain = new Paciente();
        pacienteDomain.setNombre("Jane");
        pacienteDomain.setApellido("Doe");
        pacienteDomain.setDni("12121212");
        pacienteDomain.setEmail("jane@gmail.com");
        pacienteDomain.setTelefono("1122334455");
        pacienteDomain.setDomicilio("Fake Street 123");
        pacienteDomain.setFechaNacimiento(LocalDate.of(2000, 6, 20));
        pacienteDomain.setEstadoCivil("Soltera");
        return pacienteDomain;
    }
}
