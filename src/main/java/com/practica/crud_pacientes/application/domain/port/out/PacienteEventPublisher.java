package com.practica.crud_pacientes.application.domain.port.out;

import com.practica.crud_pacientes.application.domain.model.Paciente;

public interface PacienteEventPublisher {
    void publishPaienteCreado(Paciente paciente);
    void publishPacienteEliminado(Paciente paciente);
}
