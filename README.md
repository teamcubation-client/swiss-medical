# Microservicio de Pacientes – Versión Java 11

# 🎯 Objetivo general

Implementar un microservicio RESTful en Java 11 usando Spring Boot que gestione pacientes. El proyecto debe aplicar buenas prácticas de Programación Orientada a Objetos (POO), arquitectura en capas, manejo de excepciones personalizadas, y separación entre modelo y vista mediante DTOs.

## 🧩 Fase 1: CRUD de Pacientes

### ✅ Módulo 1-A: Operaciones CRUD Básicas

Crear un microservicio que permita realizar operaciones CRUD sobre pacientes.

Los pacientes deben tener los siguientes campos:

- nombre (String)
- apellido (String)
- dni (String)
- obraSocial (String)
- email (String)
- telefono (String)

Permitir búsqueda filtrada por:

- DNI exacto
- Nombre (parcial, no sensible a mayúsculas/minúsculas)

La información de los pacientes se almacenará utilizando un repositorio (puede ser una lista en memoria, una base de datos H2 o cualquier otra implementación que se considere adecuada).

### ✅ Módulo 1-B: Manejo de Excepciones Personalizadas

Implementar excepciones personalizadas:

- PacienteNoEncontradoException: cuando se intente buscar, editar o eliminar un paciente que no existe.
- PacienteDuplicadoException: cuando se intente crear un paciente con un DNI ya existente.
- Manejar las excepciones con mensajes claros al usuario usando @ControllerAdvice o try-catch.

## 🧩 Fase 2: Arquitectura y Mejores Prácticas

### ✅ Módulo 2-A: División en Capas (Arquitectura MVC)

Dividir el proyecto en al menos tres capas:

- Modelo: contiene la clase Paciente.
- Controlador (Controller): expone los endpoints REST.
- Servicio (Service): contiene la lógica de negocio.
- Repositorio (Repository): define la abstracción de almacenamiento.
- (Opcional) Configuración, Excepciones, etc.

### ✅ Módulo 2-B: Uso de DTOs y Mappers

- Crear una clase PacienteDTO que exponga solo los datos necesarios del paciente (por ejemplo: nombre, apellido, DNI, obra social).
- Implementar una clase PacienteMapper para convertir entre Paciente y PacienteDTO.

## 🔁 Fase 3: Persistencia real (opcional o futura)

- Reemplazar la lista en memoria por una base de datos persistente utilizando Spring Data JPA.
- Configurar H2 o cualquier otra base de datos relacional.
- Agregar anotaciones como @Entity, @Id, etc. en la clase Paciente.

# 📌 Consideraciones

- El microservicio debe estar desarrollado en Java 11.
- Debe utilizar Spring Boot (versión mínima compatible con Java 11).
- Se valora el uso de @RestController, @Service, @Repository, DTOs y validaciones simples.
- El código debe ser claro, legible y estar organizado por paquetes.
