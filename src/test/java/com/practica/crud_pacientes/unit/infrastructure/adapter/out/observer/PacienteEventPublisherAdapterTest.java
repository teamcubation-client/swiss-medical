package com.practica.crud_pacientes.unit.infrastructure.adapter.out.observer;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.out.PacienteObserver;
import com.practica.crud_pacientes.infrastructure.adapter.out.observer.PacienteEventPublisherAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.practica.crud_pacientes.utils.PacienteTestFactory.buildDomain;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PacienteEventPublisherAdapterTest {

    @Mock
    private PacienteObserver observer1;

    @Mock
    private PacienteObserver observer2;

    private PacienteEventPublisherAdapter publisher;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = buildDomain();
        publisher = new PacienteEventPublisherAdapter(List.of(observer1, observer2));
    }

    @Test
    @DisplayName("Should publish when calls PacienteCreado")
    void shouldPublish_whenPacienteCreado() {
        publisher.publishPacienteCreado(paciente);

        verify(observer1, times(1)).onPacienteCreado(paciente);
        verify(observer2, times(1)).onPacienteCreado(paciente);
    }

    @Test
    @DisplayName("Should publish when calls PacienteEliminado")
    void shouldPublish_whenPacienteEliminado() {
        publisher.publishPacienteEliminado(paciente);

        verify(observer1, times(1)).onPacienteEliminado(paciente);
        verify(observer2, times(1)).onPacienteEliminado(paciente);
    }
}
