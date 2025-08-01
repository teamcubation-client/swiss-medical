package com.tq.pacientes.application.domain.model.patient.processing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;

import static com.tq.pacientes.shared.util.PatientConstants.PRE_ADOLESCENT_AGE;
import static com.tq.pacientes.shared.util.PatientConstants.PATIENT_TYPE_YOUNG;
import static com.tq.pacientes.shared.util.PatientConstants.RISK_HIGH;
import static com.tq.pacientes.shared.util.PatientConstants.RISK_LOW;
import static com.tq.pacientes.shared.util.PatientConstants.RISK_MEDIUM;
import static com.tq.pacientes.shared.util.PatientConstants.BASE_YOUNG_AGE;
import static com.tq.pacientes.shared.util.PatientConstants.KINDERGARTEN_AGE;
import static com.tq.pacientes.shared.util.PatientConstants.LOGGER_WARN_MESSAGE_PATIENT_UNDER_12_WITHOUT_HEALTH_INSURANCE;
import static com.tq.pacientes.shared.util.PatientConstants.LOGGER_INFO_SEND_NOTIFICATION;
import static com.tq.pacientes.shared.util.PatientConstants.LOGGER_INFO_PEDIATRIC_RISK_LEVEL;
import static com.tq.pacientes.shared.util.PatientConstants.LOGGER_INFO_PEDIATRIC_VACCINE_REMINDERS;
import static com.tq.pacientes.shared.util.PatientConstants.LOGGER_INFO_PEDIATRIC_CHECKUP_REMINDERS;

import static com.tq.pacientes.shared.util.PatientUtils.calculateAge;
import static org.springframework.util.StringUtils.capitalize;

public class YoungPatientSave extends PatientSaveTemplate {

    private int currentPatientAge;

    public YoungPatientSave(PatientRepositoryPort patientRepositoryPort) {
        super(patientRepositoryPort);
    }

    @Override
    protected void validate(Patient patient) {
        currentPatientAge = calculateAge(patient);
        if (currentPatientAge < PRE_ADOLESCENT_AGE && patient.getHealthInsurance() == null) {
            logger.warn(LOGGER_WARN_MESSAGE_PATIENT_UNDER_12_WITHOUT_HEALTH_INSURANCE, patient.getDni());
        }
    }

    @Override
    protected void preProcess(Patient patient) {
        patient.setFirstName(capitalize(patient.getFirstName()));
        patient.setLastName(capitalize(patient.getLastName()));
        setupAgeBasedReminders(currentPatientAge, patient);
    }

    @Override
    protected void postProcess(Patient patient) {
        logger.info(LOGGER_INFO_SEND_NOTIFICATION, PATIENT_TYPE_YOUNG, patient.getEmail());

        String riskLevel = calculatePediatricRiskLevel(currentPatientAge);
        logger.info(LOGGER_INFO_PEDIATRIC_RISK_LEVEL, patient.getDni(), riskLevel);
    }

    private void setupAgeBasedReminders(int age, Patient patient) {

        if (age < KINDERGARTEN_AGE) {
            logger.info(LOGGER_INFO_PEDIATRIC_VACCINE_REMINDERS, patient.getDni());
        } else if (age < PRE_ADOLESCENT_AGE) {
            logger.info(LOGGER_INFO_PEDIATRIC_CHECKUP_REMINDERS, patient.getDni());
        }
    }

    private String calculatePediatricRiskLevel(int age) {
        if (age < BASE_YOUNG_AGE) return RISK_HIGH;
        if (age < KINDERGARTEN_AGE) return RISK_MEDIUM;
        return RISK_LOW;
    }


}
