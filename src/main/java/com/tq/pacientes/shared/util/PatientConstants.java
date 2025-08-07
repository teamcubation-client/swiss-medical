package com.tq.pacientes.shared.util;

public class PatientConstants {

    private PatientConstants() {
    }

    public static final int BASE_YOUNG_AGE = 0;
    public static final int KINDERGARTEN_AGE = 5;
    public static final int PRE_ADOLESCENT_AGE = 12;
    public static final int LEGAL_AGE = 18;
    public static final int ADULT_AGE = 30;
    public static final int ADULT_AGE_PLUS = 40;
    public static final int ADULT_AGE_PLUS_PLUS = 50;

    public static final int BASE_SENIOR_AGE = 65;
    public static final int SENIOR_AGE = 75;
    public static final int SENIOR_AGE_PLUS = 85;

    public static final String RISK_LOW = "BAJO";
    public static final String RISK_MEDIUM = "MEDIO";
    public static final String RISK_HIGH = "ALTO";

    public static final String PATIENT_TYPE_YOUNG = "Jóven";
    public static final String PATIENT_TYPE_ADULT = "Adulto";
    public static final String PATIENT_TYPE_SENIOR = "Adulto Mayor";

    public static final String LOGGER_WARN_MESSAGE_PATIENT_UNDER_12_WITHOUT_HEALTH_INSURANCE = "Paciente menor de 12 años sin seguro médico registrado: {}";
    public static final String LOGGER_INFO_SEND_NOTIFICATION = "Notificación enviada a paciente {}: {}";
    public static final String LOGGER_INFO_PEDIATRIC_RISK_LEVEL = "Nivel de riesgo pediátrico para {}: {}";
    public static final String LOGGER_INFO_PEDIATRIC_VACCINE_REMINDERS = "Configurar recordatorios de vacunas infantiles para: {}";
    public static final String LOGGER_INFO_PEDIATRIC_CHECKUP_REMINDERS = "Configurar recordatorios de chequeos escolares para: {}";

    public static final String LOGGER_INFO_SENIOR_RISK_LEVEL = "Nivel de riesgo para {}: {}";
    public static final String LOGGER_INFO_ADULT_CHECKUP_REMINDERS = "Programar chequeos preventivos anuales para: {}";
    public static final String LOGGER_INFO_ADULT_CARDIO_SCREENING = "Incluir screening cardiovascular para: {}";
    public static final String LOGGER_INFO_ADULT_ONCO_SCREENING = "Agregar screening oncológico para: {}";

    public static final String METHOD_VALIDATE = "validate";
    public static final String METHOD_PRE_PROCESS = "preProcess";
    public static final String METHOD_POST_PROCESS = "postProcess";



}
