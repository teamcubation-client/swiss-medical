package microservice.pacientes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(properties = "spring.profiles.active=test")
class ApplicationTest {

        /**@Test
        void mainMethodShouldRunWithoutErrors() {
            Application.main(new String[] {});
        }*/

        @Test
        void contextLoads() {
            // Test básico que levanta el contexto usando H2
        }
    }
