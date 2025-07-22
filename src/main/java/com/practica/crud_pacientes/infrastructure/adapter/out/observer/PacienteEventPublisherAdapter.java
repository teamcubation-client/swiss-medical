package com.practica.crud_pacientes.infrastructure.adapter.out.observer;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.out.PacienteEventPublisher;
import com.practica.crud_pacientes.application.domain.port.out.PacienteObserver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PacienteEventPublisherAdapter implements PacienteEventPublisher {

    private final List<PacienteObserver> observers;

    public PacienteEventPublisherAdapter(List<PacienteObserver> observers) {
        this.observers = observers;
    }

    @Override
    public void publishPacienteCreado(Paciente paciente) {
        observers.forEach(observer -> observer.onPacienteCreado(paciente));
    }

    @Override
    public void publishPacienteEliminado(Paciente paciente) {
        observers.forEach(observer -> observer.onPacienteEliminado(paciente));
    }
}
