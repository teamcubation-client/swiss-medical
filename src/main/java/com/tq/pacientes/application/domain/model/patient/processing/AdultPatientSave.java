package com.tq.pacientes.application.domain.model.patient.processing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import lombok.Getter;
import org.springframework.stereotype.Component;

import static com.tq.pacientes.shared.util.PatientUtils.calculateAge;
import static org.springframework.util.StringUtils.capitalize;

@Component
@Getter
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
    }

    @Override
    protected void postProcess(Patient patient) {
    }


}
