package com.tq.pacientes.application.domain.model.patientprocessing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;

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
        if (currentPatientAge < 13 && patient.getHealthInsurance() == null) {
            logger.warn("Paciente menor de 13 años sin seguro médico registrado: {}", patient.getDni());
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
        logger.info("Notificación enviada a paciente joven: {}", patient.getEmail());

        String riskLevel = calculatePediatricRiskLevel(currentPatientAge);
        logger.info("Nivel de riesgo pediátrico para {}: {}", patient.getDni(), riskLevel);
    }

    private void setupAgeBasedReminders(int age, Patient patient) {

        if (age < 5) {
            logger.info("Configurar recordatorios de vacunas infantiles para: {}", patient.getDni());
        } else if (age < 12) {
            logger.info("Configurar recordatorios de chequeos escolares para: {}", patient.getDni());
        }
    }

    private String calculatePediatricRiskLevel(int age) {
        if (age < 1) return "ALTO";
        if (age < 5) return "MEDIO";
        return "BAJO";
    }


}
