package com.tq.pacientes.application.domain.model.patientprocessing;

import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import com.tq.pacientes.application.domain.model.Patient;

import static com.tq.pacientes.shared.util.PatientUtils.calculateAge;
import static org.springframework.util.StringUtils.capitalize;

public class SeniorPatientSave extends PatientSaveTemplate {

    private int currentPatientAge;

    public SeniorPatientSave(PatientRepositoryPort patientRepositoryPort) {
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

        String complexity = assessGeriatricComplexity(currentPatientAge);
        logger.info("Complejidad geriátrica: {} para paciente: {}", complexity, patient.getDni());

    }

    @Override
    protected void postProcess(Patient patient) {
        logger.info("Notificación enviada a paciente adulto mayor: {}", patient.getEmail());
    }

    private String assessGeriatricComplexity(int age) {
        if (age >= 85) return "ALTA";
        if (age >= 75) return "MEDIA";
        return "BAJA";
    }
}
