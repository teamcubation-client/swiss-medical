# Gestion de pacientes

En este proyecto se desarrollo una API REST para la gestion de pacientes en una clinica. Desarollado con Java 11, SpringBoot.

# Tabla de contenidos
- [Descripcion](#descripcion)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [API REST](#api-rest)
- [Estructura del proyecto](#estructura-del-proyecto)

## Descripcion

Este proyecto permite la gestion de pacientes, incluyendo operaciones para crear, listar, buscar, actualizar y eliminar pacientes. Incluye manejo de excepciones y documentacion de codigo con JavaDoc.

## Tecnologias utilizadas

    - Java 11
    - Spring Boot
    - JPA
    - Base de datos H2
    - Maven

## API REST

Los endpoints del proyecto son:
    
    - POST /api/pacientes
    Crea un nuevo paciente.
    
    - GET /api/pacientes
    Lista todos los pacientes.

    - GET /api/pacientes/{id}
    Obtiene un paciente por su ID.

    - GET /api/pacientes/buscar/dni?dni=12345678
    Busca un paciente por DNI.

    - GET /api/pacientes/buscar/nombre?nombre=Juan
    Busca pacientes por nombre.

    - DELETE /api/pacientes/{id}
    Elimina un paciente por su ID.

    - PUT /api/pacientes/{id}
    Actualiza los datos de un paciente.
    


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