package com.tq.pacientes.application.domain.model.patient.processing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import lombok.Getter;
import org.springframework.stereotype.Component;

import static com.tq.pacientes.shared.util.PatientConstants.PRE_ADOLESCENT_AGE;
import static com.tq.pacientes.shared.util.PatientConstants.RISK_HIGH;
import static com.tq.pacientes.shared.util.PatientConstants.RISK_LOW;
import static com.tq.pacientes.shared.util.PatientConstants.RISK_MEDIUM;
import static com.tq.pacientes.shared.util.PatientConstants.BASE_YOUNG_AGE;
import static com.tq.pacientes.shared.util.PatientConstants.KINDERGARTEN_AGE;

import static com.tq.pacientes.shared.util.PatientUtils.calculateAge;
import static org.springframework.util.StringUtils.capitalize;

@Component
@Getter
public class YoungPatientSave extends PatientSaveTemplate {

    private int currentPatientAge;

    public YoungPatientSave(PatientRepositoryPort patientRepositoryPort) {
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
    }

    @Override
    protected void postProcess(Patient patient) {
    }

    public String calculatePediatricRiskLevel(int age) {
        if (age < BASE_YOUNG_AGE) return RISK_HIGH;
        if (age < KINDERGARTEN_AGE) return RISK_MEDIUM;
        return RISK_LOW;
    }

    public boolean isUnder12WithoutInsurance(Patient patient) {
        return currentPatientAge < PRE_ADOLESCENT_AGE && patient.getHealthInsurance() == null;
    }

    public boolean requiresVaccineReminder(int age) {
        return age < KINDERGARTEN_AGE;
    }

    public boolean requiresCheckupReminder(int age) {
        return age >= KINDERGARTEN_AGE && age < PRE_ADOLESCENT_AGE;
    }
}
