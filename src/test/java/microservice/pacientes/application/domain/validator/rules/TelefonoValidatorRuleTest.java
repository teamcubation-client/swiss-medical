package microservice.pacientes.application.domain.validator.rules;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@DisplayName("TelefonoValidatorRule Tests")
public class TelefonoValidatorRuleTest {

    private TelefonoValidatorRule telefonoValidatorRule;

    @BeforeEach
    void setUp() {
        telefonoValidatorRule = new TelefonoValidatorRule();
    }

    @Test
    @DisplayName("Debería validar un teléfono válido de 9 dígitos")
    void validateValidTelefono() {
        Paciente paciente = Paciente.builder().telefono("123456789").build();
        assertDoesNotThrow(() -> telefonoValidatorRule.validate(paciente));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "111",
            "1 ",
            "1 2345678",
            "1 23456789",
            "1$2345678",
            "1a2345678",
            "",
            "         ",
            " ",
            "1234567890"

    })
    @DisplayName("Debería lanzar excepción para teléfono inválido")
    void validateTelefonoWithLessThan9Digits(String invalidTelefono) {
        Paciente paciente = Paciente.builder().telefono(invalidTelefono).build();
        PacienteArgumentoInvalido exception = assertThrows(PacienteArgumentoInvalido.class, () -> telefonoValidatorRule.validate(paciente));
        assertEquals("El teléfono del paciente es inválido", exception.getMessage());
    }


    @Test
    @DisplayName("Debería lanzar excepción para teléfono nulo")
    void validateNullTelefono() {
        Paciente paciente = Paciente.builder().telefono(null).build();
        assertThrows(NullPointerException.class, () -> telefonoValidatorRule.validate(paciente));
    }
}
