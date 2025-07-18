package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API-Pacientes")
                        .version("1.0.0 - SNAPSHOT")
                        .description("API de gestión de pacientes")
                );
    }

}
