package com.swissmedical.patients.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
            .info(new Info()
                    .title("Patiens API - Swiss Medical")
                    .version("1.0.0")
                    .description("API para la gestión de pacientes en el sistema Swiss Medical. Permite crear, actualizar, eliminar y consultar pacientes." +
                                 " Incluye operaciones para buscar pacientes por nombre y DNI, así como para manejar errores comunes." +
                                 " Utiliza Swagger para la documentación interactiva de la API." +
                                 " Desarrollada con Spring Boot y JPA." +
                                 " Incluye validaciones y manejo de excepciones personalizadas." +
                                 " Integración con bases de datos relacionales mediante JPA." +
                                 " Soporte para operaciones CRUD (Crear, Leer, Actualizar, Eliminar)." +
                                 " Implementa buenas prácticas de diseño de APIs RESTful." +
                                 " Compatible con OpenAPI 3.0.")
            );
  }
}
