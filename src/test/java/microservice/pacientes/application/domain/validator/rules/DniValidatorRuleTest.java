package microservice.pacientes.application.domain.validator.rules;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@DisplayName("DniValidatorRule Tests")
public class DniValidatorRuleTest {

    private DniValidatorRule dniValidatorRule;

    @BeforeEach
    void setUp() {
        dniValidatorRule = new DniValidatorRule();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345678A",
            " ",
            "1234567",
            "12345 67",
            "12345678!@#",
            "12345!@#",
            "123456789",
            "",
            "1234567",
            "        "
    })
    @DisplayName("Debería lanzar excepción para DNI inválidos")
    void validateDniWithLetters(String dni) {
        Paciente paciente = Paciente.builder().dni(dni).build();
        assertThrows(PacienteArgumentoInvalido.class, () -> dniValidatorRule.validate(paciente));
    }

    @Test
    @DisplayName("Debería lanzar excepción para DNI nulo")
    void validateNullDni() {
        String dni = null;
        Paciente paciente = Paciente.builder().dni(dni).build();
        assertThrows(NullPointerException.class, () -> dniValidatorRule.validate(paciente));
    }

    @Test
    @DisplayName("Debería validar un DNI válido")
    void validateValidDni() {
        String dni = "12345678";
        Paciente paciente = Paciente.builder().dni(dni).build();
        assertDoesNotThrow(() -> dniValidatorRule.validate(paciente));
    }

}
