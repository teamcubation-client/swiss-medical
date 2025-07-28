package com.practica.crud_pacientes.unit.infrastructure.adapter.out.observer;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.out.PacienteObserver;
import com.practica.crud_pacientes.infrastructure.adapter.out.observer.PacienteEventPublisherAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

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
        paciente = new Paciente();
        paciente.setNombre("Jane");
        paciente.setApellido("Doe");
        paciente.setDni("12121212");
        paciente.setEmail("jane@gmail.com");
        paciente.setTelefono("1122334455");
        paciente.setDomicilio("Fake Street 123");
        paciente.setFechaNacimiento(LocalDate.of(2000, 6, 20));
        paciente.setEstadoCivil("Soltera");
        publisher = new PacienteEventPublisherAdapter(List.of(observer1, observer2));
    }

    @Test
    void shouldPublishOnPacienteCreado() {
        publisher.publishPacienteCreado(paciente);

        verify(observer1, times(1)).onPacienteCreado(paciente);
        verify(observer2, times(1)).onPacienteCreado(paciente);
    }

    @Test
    void shouldPublishOnPacienteEliminado() {
        publisher.publishPacienteEliminado(paciente);

        verify(observer1, times(1)).onPacienteEliminado(paciente);
        verify(observer2, times(1)).onPacienteEliminado(paciente);
    }
}
