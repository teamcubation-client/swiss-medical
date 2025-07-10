package com.tq.pacientes;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "API Pacientes",
        version = "V1.0",
        description = "API para gestionar pacientes")
)
public class PacientesApplication {

    public static void main(String[] args) {
        SpringApplication.run(PacientesApplication.class, args);
    }

}
