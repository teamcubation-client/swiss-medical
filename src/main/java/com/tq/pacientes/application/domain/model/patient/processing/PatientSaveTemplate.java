package com.tq.pacientes.application.domain.model.patient.processing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;

public abstract class PatientSaveTemplate {

    protected final PatientRepositoryPort patientRepositoryPort;

    protected PatientSaveTemplate(PatientRepositoryPort patientRepositoryPort) {
        this.patientRepositoryPort = patientRepositoryPort;
    }

    public final void save(Patient patient) {
        validate(patient);
        preProcess(patient);
        persist(patient);
        postProcess(patient);
    }

    protected abstract void validate(Patient patient);
    protected abstract void preProcess(Patient patient);
    protected void persist(Patient patient) {
        Patient savedPatient = patientRepositoryPort.save(patient);
        patient.setId(savedPatient.getId());
    }

    protected abstract void postProcess(Patient patient);
}
