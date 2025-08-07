Microservicio de Pacientes – Versión Java 11

🎯 Objetivo general
Implementar un microservicio RESTful en Java 11 usando Spring Boot que gestione pacientes. El proyecto debe aplicar buenas prácticas de Programación Orientada a Objetos (POO), arquitectura en capas, manejo de excepciones personalizadas, y separación entre modelo y vista mediante DTOs.

🧩 Fase 1: CRUD de Pacientes
✅ Módulo 1-A: Operaciones CRUD Básicas
Crear un microservicio que permita realizar operaciones CRUD sobre pacientes.


Los pacientes deben tener los siguientes campos:


nombre (String)


apellido (String)


dni (String)


obraSocial (String)


email (String)


telefono (String)


Permitir búsqueda filtrada por:


DNI exacto


Nombre (parcial, no sensible a mayúsculas/minúsculas)


La información de los pacientes se almacenará utilizando un repositorio (puede ser una lista en memoria, una base de datos H2 o cualquier otra implementación que se considere adecuada).



✅ Módulo 1-B: Manejo de Excepciones Personalizadas
Implementar excepciones personalizadas:


PacienteNoEncontradoException: cuando se intente buscar, editar o eliminar un paciente que no existe.


PacienteDuplicadoException: cuando se intente crear un paciente con un DNI ya existente.


Manejar las excepciones con mensajes claros al usuario usando @ControllerAdvice o try-catch.



🧩 Fase 2: Arquitectura y Mejores Prácticas
✅ Módulo 2-A: División en Capas (Arquitectura MVC)
Dividir el proyecto en al menos tres capas:


Modelo: contiene la clase Paciente.


Controlador (Controller): expone los endpoints REST.


Servicio (Service): contiene la lógica de negocio.


Repositorio (Repository): define la abstracción de almacenamiento.


(Opcional) Configuración, Excepciones, etc.



✅ Módulo 2-B: Uso de DTOs y Mappers
Crear una clase PacienteDTO que exponga solo los datos necesarios del paciente (por ejemplo: nombre, apellido, DNI, obra social).


Implementar una clase PacienteMapper para convertir entre Paciente y PacienteDTO.



🔁 Fase 3: Persistencia real (opcional o futura)
Reemplazar la lista en memoria por una base de datos persistente utilizando Spring Data JPA.


Configurar H2 o cualquier otra base de datos relacional.


Agregar anotaciones como @Entity, @Id, etc. en la clase Paciente.



📌 Consideraciones
El microservicio debe estar desarrollado en Java 11.


Debe utilizar Spring Boot (versión mínima compatible con Java 11).


Se valora el uso de @RestController, @Service, @Repository, DTOs y validaciones simples.


El código debe ser claro, legible y estar organizado por paquetes.

---

# Implementación del Principio de Responsabilidad Única, patrón de diseño Observer y Mediator

## 🧱 Principio de Responsabilidad Única (SRP) y patrón Observer

Para aplicar el principio de **Responsabilidad Única (SRP)** se introdujo un requerimiento técnico: **registrar logs cada vez que se da de alta o baja a un paciente**.

Este requerimiento llevó a implementar el patrón de diseño **Observer**, agregando:

- Una interfaz `PacienteObserver` que define las acciones a realizar cuando se crea o elimina un paciente.
- Un observer concreto llamado `PacienteLoggerObserver` que se encarga **exclusivamente** de registrar logs.
- Un publisher `PacienteEventPublisherAdapter` que se encarga de notificar a todos los observers registrados.

Inicialmente, los observers estaban siendo notificados directamente desde `PacienteService`, lo cual **violaba el principio SRP**. Esto se debía a que `PacienteService` ya tenía la responsabilidad de manejar la lógica de negocio, y además estaba acoplado a la gestión de notificaciones a los observers.

### ✅ Solución: separación con EventPublisher

Para resolver esta violación, se implementó una interfaz `PacienteEventPublisher` y su correspondiente adaptador. Ahora, `PacienteService` únicamente notifica al publisher, quien se encarga de difundir el evento a todos los observers suscriptos.

Con esta estructura:

- `PacienteService` se encarga solamente de la lógica de negocio.
- `PacienteEventPublisherAdapter` se encarga de gestionar la lista de observers.
- `PacienteLoggerObserver` se encarga de generar los logs.

De esta manera, **cada clase cumple una única responsabilidad**, respetando SRP y aplicando correctamente el patrón Observer.

---

## 🤝 Patrón Mediator

Luego se agregó un segundo requerimiento: **detectar cuando hay demasiado tráfico en un canal (endpoint) y lanzar una alerta**.

Para implementar esta funcionalidad se utilizó el patrón de diseño **Mediator**, que permite centralizar la lógica de coordinación entre componentes y evita que estén directamente acoplados.

### 🔧 Implementación del Mediator

Se crearon los siguientes componentes:

- **`Mediator`** (interfaz): define un método `notifyTraffic(String event)` que se llama al detectar tráfico.
- **`TrafficMediator`**: implementación del mediator. Coordina el tráfico entre el `TrafficMonitor` (contador) y el `AlertService` (quien emite la alerta).
- **`TrafficMonitor`**: mantiene un contador de solicitudes. Cuando se supera un umbral configurado, indica que debe generarse una alerta.
- **`AlertService`**: publica el evento de alerta (que luego es recibido por los observers).
- **`SistemaLoggerObserver`**: escucha las alertas emitidas y las registra en logs.
- El endpoint `/pacientes` en el `PacienteController` es el único canal monitorizado en este caso.

### ⚙️ Flujo de ejecución

1. El controlador de pacientes (`PacienteController`) invoca `mediator.notifyTraffic("/pacientes")` cada vez que se accede al endpoint `GET /pacientes`.
2. El `TrafficMediator` consulta al `TrafficMonitor` si el tráfico superó el umbral.
3. Si se superó, se invoca al `AlertService`, quien publica un evento de alerta.
4. Este evento es recibido por todos los observers registrados, como el `SistemaLoggerObserver`.

---

## 📌 Conclusión

Este proyecto muestra cómo aplicar de forma combinada el **Principio de Responsabilidad Única**, junto con los patrones de diseño **Observer** y **Mediator**, para lograr una arquitectura limpia, desacoplada y fácil de mantener.

- ✅ Cada clase tiene una única responsabilidad clara.
- ✅ Los componentes están desacoplados entre sí.
- ✅ El sistema puede extenderse sin necesidad de modificar las clases existentes.

---

# 🩺 Health Check con Spring Boot Actuator

Este microservicio utiliza Spring Boot Actuator para exponer un endpoint de salud que permite verificar si el servicio está funcionando correctamente.

El endpoint principal es: 
- `GET /actuator/health`

También se exponen métricas de la aplicación a través del endpoint:

- `GET /actuator/metrics`

Esto permite observar información como el uso de CPU, memoria, cantidad de peticiones HTTP, estado del pool de conexiones, entre otros.