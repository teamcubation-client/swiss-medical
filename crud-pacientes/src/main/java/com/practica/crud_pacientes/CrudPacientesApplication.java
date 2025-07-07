package com.practica.crud_pacientes;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
		info = @Info(
				contact = @Contact(
						name = "Guadalupe Fernandez",
						email = "guadalupe.fernandez@teamcubation.com"
				),
				description = "OpenAPI documentation for CRUD Pacientes",
				title = "CRUD Pacientes API",
				version = "1.0"
		),
		servers = {
				@Server(
					description = "Local ENV",
					url = "http://localhost:8080"
				)
		}
)
@SpringBootApplication
public class CrudPacientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudPacientesApplication.class, args);
	}

}
