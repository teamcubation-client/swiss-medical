# Gestion de pacientes

En este proyecto se desarrollo una API REST para la gestion de pacientes en una clinica. Desarollado con Java 11, SpringBoot.

# Tabla de contenidos
- [Descripcion](#descripcion)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Estructura del proyecto](#estructura-del-proyecto)

## Descripcion

Este proyecto permite la gestion de pacientes, incluyendo operaciones para crear, listar, buscar, actualizar y eliminar pacientes. Incluye manejo de excepciones y documentacion de codigo con JavaDoc.
Tambien se hace el uso de Stored Procedures y el uso de un script para la carga de pacientes. Por ultimo, se uso Docker para armar una imagen del proyecyto.
## Tecnologias utilizadas

    - Java 11
    - Spring Boot
    - JPA
    - Base de datos MySQL
    - Maven
    - Docker

## Estructura del proyecto

```plaintext
src/
└── main/
    ├── java/
    │   └── swissmedical/
    │       ├── controller/    # Controlador REST
    │       ├── dto/           # Data Transfer Object
    │       ├── exception/     # Excepciones personalizadas y manejador global
    │       ├── mapper/        # Conversion entre entidades y DTO
    │       ├── model/         # Entidad JPA
    │       ├── repository/    # Repositorios JPA
    │       └── service/       # Logica de negocio
    └── resources/
        └── application.properties  # Conexion a la base de datos H2