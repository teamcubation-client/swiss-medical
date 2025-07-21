package com.practica.crud_pacientes.application.domain.port.out;

import com.practica.crud_pacientes.application.domain.model.Paciente;

public interface PacienteObserver {
    void onPacienteCreado(Paciente paciente);
    void onPacienteEliminado(Paciente paciente);
}
