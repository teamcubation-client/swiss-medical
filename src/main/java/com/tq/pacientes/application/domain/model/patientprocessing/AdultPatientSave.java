package com.tq.pacientes.application.domain.model.patientprocessing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;

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
        logger.info("Prioridad asignada: {} para paciente: {}", priority, patient.getDni());
    }

    @Override
    protected void postProcess(Patient patient) {
        logger.info("Notificación enviada a paciente adulto: {}", patient.getEmail());
        planPreventiveCare(currentPatientAge, patient);
    }
    private int calculateAdultPriority(int age, Patient patient) {
        int priority = 0;

        if (age >= 40) priority += 2;
        if (age >= 50) priority += 1;
        if (patient.getHealthInsurance() != null) priority += 1;

        return priority;
    }

    private void planPreventiveCare(int age, Patient patient) {
        if (age >= 30) {
            logger.info("Programar chequeos preventivos anuales para: {}", patient.getDni());
        }
        if (age >= 40) {
            logger.info("Incluir screening cardiovascular para: {}", patient.getDni());
        }
        if (age >= 50) {
            logger.info("Agregar screening oncológico para: {}", patient.getDni());
        }
    }

}
