package com.tq.pacientes.application.domain.model.patientprocessing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PatientSaveTemplate {

    protected final PatientRepositoryPort patientRepositoryPort;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public PatientSaveTemplate(PatientRepositoryPort patientRepositoryPort) {
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
        patientRepositoryPort.save(patient);
    }
    protected abstract void postProcess(Patient patient);
}
