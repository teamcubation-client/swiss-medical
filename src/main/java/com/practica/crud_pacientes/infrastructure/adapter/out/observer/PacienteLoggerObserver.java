package com.practica.crud_pacientes.infrastructure.adapter.out.observer;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.out.PacienteObserver;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PacienteLoggerObserver implements PacienteObserver {
    private static final Logger logger = LoggerFactory.getLogger("PACIENTE-LOGS");
    @Override
    public void onPacienteCreado(Paciente paciente) {
        logger.info("Paciente creado: {}", paciente);
    }

    @Override
    public void onPacienteEliminado(Paciente paciente) {
        logger.info("Paciente eliminado: {}", paciente);
    }
}
