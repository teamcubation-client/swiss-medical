package com.practica.crud_pacientes.utils;

import java.time.LocalDate;

public class TestConstants {

    public static final String ENDPOINT = "/pacientes";
    public static final int REQUEST_COUNT = 10;
    public static final int ID = 1;
    public static final String DNI = "12121212";
    public static final String EMAIL = "jane@gmail.com";
    public static final String NEW_EMAIL = "jDoe@gmail.com";
    public static final String NOMBRE = "Jane";
    public static final String APELLIDO = "Doe";
    public static final String TELEFONO = "1122334455";
    public static final String DOMICILIO = "Fake Street 123";
    public static final LocalDate FECHA_NACIMIENTO = LocalDate.of(2000, 6, 20);
    public static final String ESTADO_CIVIL = "Soltera";
    public static final String OBRA_SOCIAL = "Swiss Medical";
    public static final int LIMITE = 1;
    public static final int OFF = 0;
    public static final String PATH_BUSCAR_ID = "/pacientes/1234";
    public static final String MENSAJE_EXCEPCION_GENERICA ="Algo salió mal";
    public static final String ENTITY_NAME = "paciente";
    public static final String ERROR_FIELD_1 = "nombre";
    public static final String ERROR_MSG_1 = "El nombre es obligatorio";
    public static final String ERROR_FIELD_2 = "dni";
    public static final String ERROR_MSG_2 = "El DNI no es válido";
    public static final String ERROR_MSG_EXCEPTION = "Errores de validacion";
    public static final String WARN_LEVEL = "WARN";
    public static final String INFO_LEVEL = "INFO";
    public static final String HIGH_TRAFFIC_MESSAGE = "High traffic detected";
    public static final String PACIENTE_CREADO_MESSAGE = "Paciente creado";
    public static final String PACIENTE_ELIMINADO_MESSAGE = "Paciente eliminado";


}
