package com.practica.crud_pacientes.utils;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteRequest;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteResponse;

import static com.practica.crud_pacientes.utils.TestConstants.*;

public class PacienteTestFactory {
    public static PacienteRequest buildRequest() {
        PacienteRequest request = new PacienteRequest();
        request.setNombre(NOMBRE);
        request.setApellido(APELLIDO);
        request.setDni(DNI);
        request.setObraSocial(OBRA_SOCIAL);
        request.setEmail(EMAIL);
        request.setTelefono(TELEFONO);
        request.setDomicilio(DOMICILIO);
        request.setFechaNacimiento(FECHA_NACIMIENTO);
        request.setEstadoCivil(ESTADO_CIVIL);
        return request;
    }

    public static PacienteResponse buildResponse() {
        PacienteResponse response = new PacienteResponse();
        response.setNombre(NOMBRE);
        response.setApellido(APELLIDO);
        response.setDni(DNI);
        response.setObraSocial(OBRA_SOCIAL);
        response.setEmail(EMAIL);
        response.setTelefono(TELEFONO);
        response.setDomicilio(DOMICILIO);
        response.setFechaNacimiento(FECHA_NACIMIENTO);
        response.setEstadoCivil(ESTADO_CIVIL);
        return response;
    }
    public static Paciente buildDomain() {
        Paciente pacienteDomain = new Paciente();
        pacienteDomain.setNombre(NOMBRE);
        pacienteDomain.setApellido(APELLIDO);
        pacienteDomain.setDni(DNI);
        pacienteDomain.setEmail(EMAIL);
        pacienteDomain.setTelefono(TELEFONO);
        pacienteDomain.setDomicilio(DOMICILIO);
        pacienteDomain.setFechaNacimiento(FECHA_NACIMIENTO);
        pacienteDomain.setEstadoCivil(ESTADO_CIVIL);
        return pacienteDomain;
    }
}
