package com.tq.pacientes.application.domain.model.patient.processing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;

import static com.tq.pacientes.shared.util.PatientConstants.LOGGER_INFO_SEND_NOTIFICATION;
import static com.tq.pacientes.shared.util.PatientConstants.LOGGER_INFO_ADULT_RISK_LEVEL;
import static com.tq.pacientes.shared.util.PatientConstants.LOGGER_INFO_ADULT_CHECKUP_REMINDERS;
import static com.tq.pacientes.shared.util.PatientConstants.LOGGER_INFO_ADULT_CARDIO_SCREENING;
import static com.tq.pacientes.shared.util.PatientConstants.LOGGER_INFO_ADULT_ONCO_SCREENING;
import static com.tq.pacientes.shared.util.PatientConstants.PATIENT_TYPE_ADULT;
import static com.tq.pacientes.shared.util.PatientConstants.ADULT_AGE;
import static com.tq.pacientes.shared.util.PatientConstants.ADULT_AGE_PLUS;
import static com.tq.pacientes.shared.util.PatientConstants.ADULT_AGE_PLUS_PLUS;
import static com.tq.pacientes.shared.util.PatientConstants.HIGH_PRIORITY;
import static com.tq.pacientes.shared.util.PatientConstants.LOW_PRIORITY;
import static com.tq.pacientes.shared.util.PatientConstants.MEDIUM_PRIORITY;
import static com.tq.pacientes.shared.util.PatientUtils.calculateAge;
import static org.springframework.util.StringUtils.capitalize;

public class AdultPatientSave extends PatientSaveTemplate {

    private int currentPatientAge;

    public AdultPatientSave(PatientRepositoryPort patientRepositoryPort) {
        super(patientRepositoryPort);
    }

    @Override
    protected void validate(Patient patient) {
        currentPatientAge = calculateAge(patient);
    }

    @Override
    protected void preProcess(Patient patient) {
        patient.setFirstName(capitalize(patient.getFirstName()));
        patient.setLastName(capitalize(patient.getLastName()));

        int priority = calculateAdultPriority(currentPatientAge, patient);
        logger.info(LOGGER_INFO_ADULT_RISK_LEVEL, priority, patient.getDni());
    }

    @Override
    protected void postProcess(Patient patient) {
        logger.info(LOGGER_INFO_SEND_NOTIFICATION, PATIENT_TYPE_ADULT, patient.getEmail());
        planPreventiveCare(currentPatientAge, patient);
    }
    private int calculateAdultPriority(int age, Patient patient) {
        int priority = LOW_PRIORITY;

        if (age >= ADULT_AGE_PLUS || patient.getHealthInsurance() != null) priority += MEDIUM_PRIORITY;
        if (age >= ADULT_AGE_PLUS_PLUS) priority += HIGH_PRIORITY;

        return priority;
    }

    private void planPreventiveCare(int age, Patient patient) {
        if (age >= ADULT_AGE) {
            logger.info(LOGGER_INFO_ADULT_CHECKUP_REMINDERS, patient.getDni());
        }
        if (age >= ADULT_AGE_PLUS) {
            logger.info(LOGGER_INFO_ADULT_CARDIO_SCREENING, patient.getDni());
        }
        if (age >= ADULT_AGE_PLUS_PLUS) {
            logger.info(LOGGER_INFO_ADULT_ONCO_SCREENING, patient.getDni());
        }
    }

}
