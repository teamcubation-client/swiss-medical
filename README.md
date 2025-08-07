# Microservicio de Pacientes – Versión Java 11

## 🎯 Objetivo general

Implementar un microservicio RESTful en Java 11 usando Spring Boot que gestione pacientes. El proyecto debe aplicar
buenas prácticas de Programación Orientada a Objetos (POO), arquitectura en capas, manejo de excepciones personalizadas,
y separación entre modelo y vista mediante DTOs.

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

La información de los pacientes se almacenará utilizando un repositorio (puede ser una lista en memoria, una base de
datos H2 o cualquier otra implementación que se considere adecuada).

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

- Crear una clase PacienteDTO que exponga solo los datos necesarios del paciente (por ejemplo: nombre, apellido, DNI,
  obra social).
- Implementar una clase PacienteMapper para convertir entre Paciente y PacienteDTO.

## 🔁 Fase 3: Persistencia real (opcional o futura)

- Reemplazar la lista en memoria por una base de datos persistente utilizando Spring Data JPA.
- Configurar H2 o cualquier otra base de datos relacional.
- Agregar anotaciones como @Entity, @Id, etc. en la clase Paciente.

> [!NOTE]
> 📌 Consideraciones

- El microservicio debe estar desarrollado en Java 11.
- Debe utilizar Spring Boot (versión mínima compatible con Java 11).
- Se valora el uso de @RestController, @Service, @Repository, DTOs y validaciones simples.
- El código debe ser claro, legible y estar organizado por paquetes.

## Fase 4: Documentación y Mensajería

### ✅ Parte A: Documentación de la API con Swagger (SpringDoc OpenAPI)

- 📚 Objetivo: Documentar de manera automática los endpoints del microservicio usando Swagger, permitiendo probarlos
  desde una interfaz web (/swagger-ui.html o /swagger-ui/index.html).
- Instalar la dependencia springdoc-openapi-ui.
- Probar que aparezca la documentación.
- (Opcional) Agregar anotaciones @Operation, @Parameter o @ApiResponse para enriquecer la docu.
- 🧠 Beneficio: les ayuda a visualizar los endpoints y compartir su API con otros fácilmente.

### ✅ Parte B: Centralización de Excepciones con @ControllerAdvice

- 📚 Objetivo: Implementar una clase global que maneje las excepciones personalizadas del microservicio, devolviendo
  mensajes claros y estados HTTP adecuados.
- Crear una clase GlobalExceptionHandler.
- Usar @ExceptionHandler para capturar PacienteNoEncontradoException y PacienteDuplicadoException.
- Retornar objetos ResponseEntity con código de error y mensaje.
- 🧠 Beneficio: muestra una práctica real de cómo se manejan errores de forma profesional en aplicaciones REST.

## 🐳 Fase 5: Contenerización con Docker + MySQL

### 🎯 Objetivo

- Contenerizar el microservicio usando Docker. Sustituir la base de datos H2 en memoria por una base de datos MySQL
  montada también como contenedor. Usar Docker Compose para orquestar ambos contenedores (app + db) y un archivo .env
  para manejar variables de entorno.

## Fase 6: Implementación e Integración de Stored Procedures

### 🎯 Objetivo

- Extender el microservicio para incluir lógica almacenada directamente en la base de datos. En esta etapa, deberán:
- Crear y poblar la base de datos MySQL con registros de pacientes.
- Implementar procedimientos almacenados (stored procedures) para consultas específicas.
- Invocar esos procedimientos desde el repositorio en Java.
- Exponerlos a través de la API REST de forma limpia y documentada.

### ✅ Módulo 6-A: Crear script init.sql

1. Crear un archivo llamado init.sql con el siguiente contenido:
    - Carga de datos de ejemplo: al menos 10 registros realistas en la tabla paciente.

    - Definición de stored procedures:
        - `buscar_paciente_por_dni(IN p_dni VARCHAR(20))`: devuelve un único paciente por DNI.
        - `buscar_pacientes_por_nombre(IN p_nombre VARCHAR(50))`: devuelve pacientes cuyo nombre contenga el valor
          parcial recibido (insensible a mayúsculas).
        - `buscar_pacientes_por_obra_social_paginado(IN p_obra_social VARCHAR(50), IN p_limit INT, IN p_offset INT)`:
          devuelve pacientes de una obra social con paginación estilo limit/offset.

```sql
init.sql
-- Datos de ejemplo para la tabla paciente
INSERT INTO paciente (dni, nombre, apellido, obra_social, email, telefono) VALUES
('12345678', 'Carlos', 'Pérez', 'OSDE', 'carlos.perez@example.com', '111-1111'),
('23456789', 'Ana', 'Gómez', 'Swiss Medical', 'ana.gomez@example.com', '222-2222'),
('34567890', 'Luis', 'Martínez', 'OSDE', 'luis.martinez@example.com', '333-3333'),
('45678901', 'María', 'López', 'Galeno', 'maria.lopez@example.com', '444-4444'),
('56789012', 'Jorge', 'Sánchez', 'OSDE', 'jorge.sanchez@example.com', '555-5555'),
('67890123', 'Lucía', 'Fernández', 'Swiss Medical', 'lucia.fernandez@example.com', '666-6666'),
('78901234', 'Pedro', 'Ramírez', 'Medicus', 'pedro.ramirez@example.com', '777-7777'),
('89012345', 'Laura', 'Suárez', 'Galeno', 'laura.suarez@example.com', '888-8888'),
('90123456', 'Sofía', 'Gutiérrez', 'OSDE', 'sofia.gutierrez@example.com', '999-9999'),
('01234567', 'Diego', 'Herrera', 'Medicus', 'diego.herrera@example.com', '101-0101');

-- Procedimiento 1: buscar paciente por DNI
DELIMITER //

CREATE PROCEDURE buscar_paciente_por_dni(IN p_dni VARCHAR(20))
BEGIN
SELECT dni, nombre, apellido, obra_social, email, telefono
FROM paciente
WHERE dni = p_dni;
END;
//

-- Procedimiento 2: buscar pacientes por nombre parcial (case-insensitive)
CREATE PROCEDURE buscar_pacientes_por_nombre(IN p_nombre VARCHAR(50))
BEGIN
SELECT dni, nombre, apellido, obra_social, email, telefono
FROM paciente
WHERE LOWER(nombre) LIKE CONCAT('%', LOWER(p_nombre), '%');
END;
//

-- Procedimiento 3: buscar pacientes por obra social con paginación
CREATE PROCEDURE buscar_pacientes_por_obra_social_paginado(
IN p_obra_social VARCHAR(50),
IN p_limit INT,
IN p_offset INT
)
BEGIN
SELECT dni, nombre, apellido, obra_social, email, telefono
FROM paciente
WHERE obra_social = p_obra_social
LIMIT p_limit OFFSET p_offset;
END;
//

DELIMITER ;
```

2. Configurar docker-compose.yml para que el contenedor MySQL ejecute automáticamente init.sql al inicializarse

```yaml
volumes:
  - ./01_init.sql:/docker-entrypoint-initdb.d/01_init.sql
```

3. Definir el archivo .env con las variables necesarias para la base de datos.

### ✅ Módulo 6-B: Invocar los Stored Procedures desde Java

1. En la interfaz PacienteRepository, agregar métodos que ejecuten los stored procedures utilizando @Query(
   value = "...", nativeQuery = true) o @Procedure.

```java

@Query(value = "CALL buscar_paciente_por_dni(:dni)", nativeQuery = true)
Optional<Paciente> buscarPorDniConSP(@Param("dni") String dni);
```

2. Desde la capa de servicios, invocar los métodos del repositorio y mapear los resultados a PacienteDTO.
3. Manejar posibles errores (por ejemplo, si no se encuentra un paciente) usando excepciones personalizadas como
   PacienteNoEncontradoException.

### ✅ Módulo 6-C: Exponer Endpoints REST

- Agregar endpoints en el controlador que permitan consultar los stored procedures desde la API REST. Ejemplo de
  endpoints:

| Endpoint                   | Método | Descripción                                    |
|----------------------------|--------|------------------------------------------------|
| /pacientes/dni/{dni}       | GET    | Busca un paciente por DNI (via SP)             |
| /pacientes/nombre/{nombre} | GET    | Busca pacientes por nombre parcial (via SP)    |
| /pacientes/obra-social     | GET    | Busca pacientes por obra social con paginación |

- El endpoint /obra-social debe recibir los parámetros:
    - `obraSocial`, `page`, `size` como `@RequestParam`

### 📌 Requisitos técnicos

- Usar MySQL en Docker, reemplazando la base en memoria.
- Spring Boot con Spring Data JPA.
- El script SQL debe ejecutarse automáticamente en la creación del contenedor.
- Resolver las búsquedas solicitadas usando exclusivamente los stored procedures implementados.

## 🧩 Fase 7: Migración a Arquitectura Hexagonal (Ports & Adapters)

### 🎯 Objetivo

- Reestructurar el microservicio para adoptar una arquitectura hexagonal, también conocida como Arquitectura de Puertos
  y Adaptadores, donde el dominio central queda aislado de las tecnologías externas (frameworks, base de datos, web,
  etc.).
-

### ✅ Módulo 7-A: Propuesta de Estructura de Carpetas

```bash
src/
└── main/
    └── java/
        └── microservice/
            └── pacientes/
                ├── application/
                │   ├── domain/
                │   │   ├── model/
                │   │   │   └── Paciente.java
                │   │   └── port/
                │   │       ├── in/
                │   │       └── out/
                │   └── service/
                │       └── PacienteService.java
                ├── infrastructure/
                │   └── adapter/
                │       ├── in/rest/
                │       └── out/persistence/
                └── shared/
                    └── ...
```

### ✅ Módulo 7-B: Principios a Aplicar

- La capa `domain` es el núcleo del sistema, y no depende de ninguna tecnología.
    - Contiene los modelos (Paciente) y los puertos (port/in y port/out).
    - Los puertos definen interfaces, no implementaciones.
- La capa `application` implementa la lógica de negocio (los casos de uso) orquestando los puertos.
- La capa `infrastructure` contiene los adaptadores que se conectan a tecnologías externas como Spring MVC, JPA,
  Swagger, etc.
    - `adapter/in/rest` conecta el mundo web (controllers).
    - `adapter/out/persistence` conecta con la base de datos (repositorios, entidades JPA, mapeadores).
- shared agrupa componentes transversales como excepciones.

### ✅ Módulo 7-C: Migración guiada a Arquitectura Hexagonal

- Migrá el proyecto desde MVC a una arquitectura hexagonal basada en puertos y adaptadores, reorganizando
  responsabilidades según las siguientes acciones:

#### 📌 Pasos a seguir

1. Modelo de dominio (Paciente)
    - Ubicación: application.domain.model
    - Representa el paciente dentro del negocio.
    - Sin anotaciones de frameworks.
2. Definir puertos (interfaces)
    - port.in: PacienteUseCase define las operaciones disponibles.
    - port.out: PacienteRepositoryPort define cómo se accede a los datos.
3. Casos de uso (PacienteService)
    - Ubicación: application.service
    - Implementa PacienteUseCase, usa PacienteRepositoryPort.
4. Controlador REST
    - Ubicación: infrastructure.adapter.in.rest
    - Se conecta solo con PacienteUseCase. No accede a entidades ni repos directamente.
5. Adapter de persistencia
    - Ubicación: infrastructure.adapter.out.persistence
    - Implementa PacienteRepositoryPort usando JpaRepository y stored procedures.
    - Incluye clase PacienteEntity con anotaciones JPA.
6. DTOs REST (PacienteRequest, PacienteResponse)
    - Ubicación: infrastructure.adapter.in.rest.dto
    - Modelos para entrada y salida de la API.
    - Solo contienen los datos necesarios para el cliente.
    - Permiten cambiar la interfaz sin afectar al dominio ni a la base de datos.
7. Mappers
    - Ubicación según el adapter correspondiente (rest.mapper, persistence.mapper)
    - Transforman entre modelos:
        - REST ↔ Dominio
        - Persistencia ↔ Dominio

## 🧠 Buenas prácticas a reforzar

- Cada capa solo conoce las capas más internas.
- El dominio es independiente de tecnología.
- Los puertos son contratos; los adapters los implementan.
- Los DTOs y entidades son detalles técnicos, no parte del modelo central.

## 🧩 Fase 8: Principios SOLID + Patrones de Diseño

### 🎯 Objetivo

- Refactorizar el microservicio implementando explícitamente los principios SOLID y patrones de diseño. Cada miembro del
  equipo debe elegir un principio SOLID y patrón específico, investigar su aplicación, implementarlo y presentar su
  solución al resto del equipo explicando las mejoras introducidas.
- 📋 Tabla de Asignación de Principios y Patrones

| Miembro del Equipo | Principio SOLID                 | Patrón de diseño |
|--------------------|---------------------------------|------------------|
| Dani               | Liskov Substitution Principle   | Factory Method   |
| Emi                | Segregación de Interfaces       | Proxy            |
| Fer                | Dependency Injection (DI)       | Template Method  |
| Guada              | Single Responsibility Principle | Observer         |
| Marcos             |                                 |                  |
| Nico               | Open/Closed Principle (OCP)     | Strategy         |

## ✅ Módulo 8-A: Implementación de Principios SOLID

### 📌 Tareas por Principio SOLID

- Cada miembro que elija un principio SOLID debe:
    - Investigar el principio asignado y comprender su propósito
    - Identificar en el código actual del microservicio dónde se viola el principio
    - Implementar una refactorización que demuestre la aplicación correcta del principio
    - Crear un ejemplo de código "antes" y "después" que muestre claramente la diferencia
    - Preparar una explicación para presentar al equipo sobre el principio implementado

## ✅ Módulo 8-B: Implementación de Patrones de Diseño

### 📌 Tareas por Patrón de Diseño

- Cada miembro que elija un patrón de diseño debe:
- Investigar el patrón asignado, su estructura y casos de uso típicos
- Identificar una situación específica en el microservicio donde el patrón sea aplicable
- Implementar el patrón de manera que resuelva un problema real del proyecto
- Demostrar cómo el patrón mejora la flexibilidad, mantenibilidad o extensibilidad del código
- Preparar una explicación para presentar al equipo sobre el patrón implementado

## 🧠 Beneficios Esperados

- Al completar esta fase, el equipo habrá:
- Aplicado principios SOLID de manera práctica y comprensible
- Implementado patrones de diseño que resuelvan problemas reales
- Mejorado la calidad del código mediante refactoring sistemático
- Desarrollado habilidades de investigación y aplicación de conceptos teóricos
- Fortalecido las habilidades de comunicación técnica y trabajo en equipo
-

## 📌 Consideraciones Técnicas

- Mantener compatibilidad con la arquitectura hexagonal existente
- Actualizar la documentación de Swagger si es necesario
- Conservar la funcionalidad de los stored procedures
- Mantener la contenerización con Docker

## 🧪 Fase 9: Testing del Microservicio

### 🎯 Objetivo General

- Alcanzar al menos un 80% de cobertura de código mediante pruebas automatizadas que validen el comportamiento del
  dominio, los casos de uso, los adaptadores y los endpoints del microservicio.

## ✅ Módulo 9-A: Tests Unitarios con Arquitectura Hexagonal

### 📌 Objetivo:

- Probar en forma aislada cada unidad de lógica del sistema, mockeando las dependencias externas mediante los puertos (
  interfaces).
- La clave es testear desde el interior hacia el exterior:

| Capa                    | Clase principal          | Qué probar                                     | Cómo testear                             |
|-------------------------|--------------------------|------------------------------------------------|------------------------------------------|
| application/service     | PatientService           | Casos de uso: crear, buscar, eliminar          | Mock PatientRepositoryPort               |
| adapter/in/rest         | PatientController        | Mapeo HTTP ↔ Dominio + manejo de errores       | Mock PatientUseCase                      |
| adapter/out/persistence | PatientRepositoryAdapter | Persistencia y mapeo entre entidades y dominio | Mock JpaRepository y PatientEntityMapper |

#### 📘 Ejemplo 1: PatientServiceTest.java

```java

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

  @Mock
  private PatientRepositoryPort repository;

  @InjectMocks
  private PatientService service;

  @Test
  void shouldCreatePatientWhenDniIsNotTaken() {
    Patient patient = new Patient("123", "Ana", "Lopez", "OSDE", "ana@mail.com", "123456");

    when(repository.existsByDni("123")).thenReturn(false);
    when(repository.save(any())).thenReturn(patient);

    Patient result = service.createPatient(patient);

    assertEquals("Ana", result.getFirstName());
    verify(repository).save(patient);
  }

  @Test
  void shouldThrowDuplicatedExceptionIfDniExists() {
    Patient patient = new Patient("123", "Ana", "Lopez", "OSDE", "ana@mail.com", "123456");

    when(repository.existsByDni("123")).thenReturn(true);

    assertThrows(PatientDuplicatedException.class, () -> {
      service.createPatient(patient);
    });
  }

}
```

#### 📘 Ejemplo 2: PatientControllerTest.java

```java

@WebMvcTest(PatientController.class)
class PatientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PatientUseCase useCase;

  @Test
  void shouldReturnPatientByDni() throws Exception {
    Patient patient = new Patient("123", "Ana", "Lopez", "OSDE", "ana@mail.com", "123456");

    when(useCase.findByDni("123")).thenReturn(patient);

    mockMvc.perform(get("/patients/dni/123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Ana"));
  }

}
```

#### 📘 Ejemplo 3: PatientRepositoryAdapterTest.java

```java

@ExtendWith(MockitoExtension.class)
class PatientRepositoryAdapterTest {

  @Mock
  private PatientJpaRepository jpaRepository;

  @Mock
  private PatientEntityMapper entityMapper;

  @InjectMocks
  private PatientRepositoryAdapter adapter;

  @Test
  void shouldSavePatientSuccessfully() {
    Patient patient = new Patient("123", "Ana", "Lopez", "OSDE", "ana@mail.com", "123456");
    PatientEntity entity = new PatientEntity();

    when(entityMapper.toEntity(patient)).thenReturn(entity);
    when(jpaRepository.save(entity)).thenReturn(entity);
    when(entityMapper.toDomain(entity)).thenReturn(patient);

    Patient result = adapter.save(patient);

    assertEquals("123", result.getDni());
  }

}
```

## ✅ Módulo 9-B: Tests de Integración

### 📌 Objetivo:

- Probar que los componentes se integran correctamente en conjunto, especialmente desde el controlador hasta la
  persistencia.
- Recomendaciones:
- Usar @SpringBootTest o @WebMvcTest con MockMvc.
- Crear un perfil de test (application-test.yml) para evitar la conexión a MySQL real.
- Se puede mockear el acceso a base de datos o usar H2 en memoria para simular procedimientos almacenados si es
  necesario.

- 📌 Si quieren pruebas realistas con base de datos, pueden usar Testcontainers, aunque no es obligatorio para este
  ejercicio.

### 📁 Estructura sugerida de tests

```
src/test/java/com/swissmedical/pacients/
        ├──application/
        │ └──service/
        │   └──PatientServiceTest.java
        ├──infrastructure/
            │ └──adapter/
            │ ├──in/rest/controller/
            │ │ └──PatientControllerTest.java
            │ └──out/persistence/mysql/repository/
            │ └──PatientRepositoryAdapterTest.java
        └──shared/
        └──exceptions/
        └──GlobalExceptionHandlerTest.java (opcional)
```

## 📌 Herramientas y configuración

- Usar JUnit 5, Mockito, MockMvc.
- Medir cobertura con Jacoco (jacoco-maven-plugin o equivalente).
- Verificar que la cobertura supere el 80% en las capas de dominio, aplicación y adaptadores.

## 🧠 Beneficios esperados

- Validación clara del comportamiento esperado y no esperado.
- Posibilidad de refactorizar sin miedo.
- Cobertura sólida en lógica de negocio.
- Separación de responsabilidades probadas.

## 🧩 Fase 10: Logging y Observabilidad

### 🎯 Objetivo

- Agregar logs al microservicio para poder entender su comportamiento, detectar errores y seguir el flujo de ejecución
  sin depurar con el IDE.

### 🔧 Tecnología sugerida

- Usar el sistema de logs por defecto de Spring Boot: SLF4J + Logback.

Para loggear, usar:

- `Logger logger = LoggerFactory.getLogger(...)`
- o `@Slf4j` de Lombok.

- 🧪 Actividad
- Agregá logs en partes clave de tu app (controller, service, repository, excepciones).
- Usá diferentes niveles de log:
    - info: eventos normales (ej: "Paciente creado").
    - warn: situaciones sospechosas.
    - error: cuando ocurre una excepción.
- Ejecutá la app y observá los logs en consola.

_Reflexioná_:

- ¿Qué nivel de log usaste más?
- ¿Te ayudaron los logs a entender el flujo?
- ¿Qué mejorarías?
- ¿Qué datos sería mejor no loggear?

### ✅ Requisitos mínimos

- Al menos 2 niveles de log implementados.
- Logs visibles en consola.
- Poder explicar por qué se loggea en cada parte.

- 🧠 Sugerencia: explorá cómo cambiar el nivel de logging desde application.yml.

---

# 📝 Check List

- [ x ] Implementar operaciones CRUD a pacientes
- [ x ] Manejo de excepciones personalizadas
- [ x ] Dividir el proyecto en capas (Modelo, Controlador, Servicio, Repositorio)
- [ x ] Uso de DTOs y Mappers
- [ x ] Crear requests de prueba
- [ x ] Documentar el API con Swagger
- [ x ] Crear el archivo Dockerfile para el `build` de la aplicación
- [ x ] Crear el archivo docker-compose.yml para contenerizar la aplicación y la base de datos MySQL

## Estructura del Proyecto

```
patients/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/swissmedical
│   │   │   │   ├── patients/
│   │   │   │   │   ├── controller/
│   │   │   │   │   ├── dto/
│   │   │   │   │   ├── exception/
│   │   │   │   │   ├── entity/
│   │   │   │   │   ├── mappers/
│   │   │   │   │   ├── repository/
│   │   │   │   │   ├── service/
│   │   │   │   │   └── PatientsApplication.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── static
│   │   │   └── templates
│   └── test/
│       └── java/
│           └── com/swissmedical/patients/
│               └── PatientsApplicationTests.java
└── pom.xml
docker-compose.yml
Dockerfile
README.md
```

---

# Comentarios del archivo docker-compose.yml

- Para que dos contenedores estén comunicados, es necsario que estén en la misma red
- Es necesario definir la variable de entorno de `host` para que la aplicación de Spring identifique correctamente la
  base de datos MySQL
- La sección de `healthcheck` sirve para verificar que el servicio de MySQL esté listo antes de que la aplicación Spring
  Boot intente conectarse a él
- Esto es importante para evitar errores de conexión al iniciar la aplicación
- Al `depends_on` se le agrega el `condition: service_healthy` para que la aplicación espere a que el servicio de MySQL
  esté completamente operativo antes de iniciar
- A lo último se define las redes que se va a utilizar en el contenedor

## Detalles sobre el apartado `healthcheck`

- `test`: Comando que se ejecuta para verificar la salud del contenedor. En este caso, se usa `mysqladmin ping` para
  comprobar si el servidor MySQL está respondiendo.
- `timeout`: Tiempo máximo que se espera para que el comando se ejecute. Si el comando no responde en este tiempo, se
  considera que la verificación ha fallado.
- `retries`: Número de intentos que se realizan antes de considerar que el contenedor no está saludable.
- `interval`: Tiempo entre cada intento de verificación de salud.
- `start_period`: Tiempo que se espera antes de comenzar a realizar verificaciones de salud. Esto es útil para dar
  tiempo al contenedor para iniciar completamente antes de que se realicen las primeras verificaciones.

## Driver `bridges`

- El driver `bridge` es el controlador de red predeterminado en Docker
- Permite que los contenedores se comuniquen entre sí y con el host, creando una red aislada para los contenedores que
  se ejecutan en el mismo host

```yaml
services:
  # Imagen de la Base de Datos
  mysql:
    image: mysql:8.0-debian
    container_name: spring_db
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
    ports:
      - "${MYSQL_PORT}:3306"
    volumes:
      - ./db_data:/var/lib/mysql
    networks:
      - app-net
    healthcheck:
      test: [ "CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-p$$MYSQL_ROOT_PASSWORD" ]
      timeout: 20s
      retries: 10
      interval: 10s
      start_period: 60s  # Aumentado para dar más tiempo

  app:
    build: .
    container_name: spring_app
    environment:
      MYSQL_HOST: mysql
      MYSQL_PORT: ${MYSQL_PORT}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
      MYSQL_ROOT_USER: ${MYSQL_ROOT_USER}
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_PARAMS: ${MYSQL_PARAMS}
    ports:
      - "8000:8000"
    depends_on:
      mysql:
        condition: service_healthy
    networks:
      - app-net

networks:
  app-net:
    driver: bridge
```

---

# Implementación de Store Procedures

- Se creó los archivos `init.sql` y `stored_procedures.sql` para definir los procedimientos almacenados y poblar la base
  de datos con datos de ejemplo.
- Se aplicó la nomeclatura con un número al inicio de cada archivo para asegurar que se ejecuten en el orden correcto al
  iniciar el contenedor de MySQL.
- Se configuró el archivo `docker-compose.yml` para montar estos archivos en el directorio
  `/docker-entrypoint-initdb.d/` del contenedor MySQL, lo que permite que se ejecuten automáticamente al iniciar el
  contenedor.
- Dentro del repositorio se utilizó la anotación `@Query` con `nativeQuery = true` para invocar los procedimientos
  almacenados desde Java.

```java

@Query(value = "CALL buscar_paciente_por_dni(:dni)", nativeQuery = true)
Optional<Paciente> buscarPorDniConSP(@Param("dni") String dni);
```

- No olvidar realizar el `build` de la aplicación y el `docker-compose up` para que se creen los contenedores y se
  ejecuten los scripts de inicialización.

---

# Refactorización a Arquitectura Hexagonal

- Se reorganizó el proyecto para adoptar una arquitectura hexagonal, separando claramente las capas de dominio y de
  infrastructura.
- Se definieron los puertos de entrada y salida para el dominio, permitiendo que la lógica de negocio no dependa de
  detalles técnicos como JPA o REST.
- Se implementaron adaptadores para conectar el dominio con la infraestructura, manteniendo el núcleo del negocio
  independiente de las tecnologías externas.

## Nueva Estructura del Proyecto

```bash
src/
└── main/
    └── java/
        └── com.swissmedical/
            └── pacients/
                ├── application/
                │   ├── domain/
                │   │   ├── model/
                │   │   │   └── Patient.java
                │   │   └── port/
                │   │       ├── in/
                |   |           └── PatienteUseCase.java
                │   │       └── out/
                │   │           └── PatienteRepositoryPort.java
                │   └── service/
                │       └── PatienteService.java
                ├── infrastructure/
                │   ├── adapter/
                │   |    ├── in/rest/
                │   |    │   ├── controller/
                │   |    │   |   ├── PatientApi.java
                │   |    │   |   ├── PatientController.java
                │   |    │   |   └── RootController.java 
                │   |    │   └── dto/
                │   |    │   |   ├── PatientCreateDto.java
                │   |    │   |   ├── PatientUpdateDto.java
                │   |    │   |   └── PatientResponseDto.java
                │   |    │   └── mapper/ 
                │   |    │   |   ├── PatientCreateMapper.java
                │   |    │   |   ├── PatientUpdateMapper.java
                │   |    │   |   └── PatientResponseMapper.java
                │   |    └── out/persistence/mysql/
                │   |        ├── entity/
                │   |        │   └── PatientEntity.java
                │   |        ├── mapper/
                │   |        │   └── PatientEntityMapper.java
                │   |        └── repository/
                │   |            ├── PatientJpaRepository.java
                │   |            └── PatientRepositoryAdapter.java
                │   └── config/
                │       └── OpenApiConfig.java
                └── shared/
                    └── exceptions/
                    |   ├── GlobalExceptionHandler.java
                    |   ├── PatientNotFoundException.java
                    |   └── PatientDuplicatedException.java
                    └── utils/
                        ├── ErrorMessages.java
                        └── ResponseCode.java
                    
```

---

# Implementación de SOLID y Patrones de Diseño

## Segregación de interfaces (Intefaces segregation)

- Este principio establece que se deben separar en interfaces diferentes métodos con funciones específicas
- En esta ocasión, separé en interfaces cada caso de uso (Use Case) dependeniendo su método
    - `CreatePatientUseCase`: Se encapsulan los métodos relacionados con crear datos
    - `ReadPatientUseCase`: Se encapsulan los métodos relacionados con obtener datos
    - `UpdatePatientUseCase`: Se encapsulan los métodos relacionados con actualizar datos
    - `DeletePatientUseCase`: Se encapsulan los métodos relacionados con eliminar datos

## Patrón Proxy

- Para mi elección de Patrón de Diseño, implementé el patrón Proxy para la clase `PatientController`
- En este caso, se utilizó Programación Orientada a Aspectos (AOP) para crear un proxy que envuelve al controlador
- Anotaciones utilizadas:
    - `@Aspect`: Define la clase como un aspecto de AOP
    - `@Around`: Define un método que se ejecuta alrededor de la ejecución de un método
    - `within`: Especifica que el aspecto se aplica a métodos con la anotación `@RestController`
- Se implementó un `logger` para ejecutarse cada vez que se realiza una petición al controlador, registrando el nombre
  del método y los parámetros recibidos

```java

@Aspect
@Component
public class LoggingAspect {

  private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

  @Around("within(@org.springframework.web.bind.annotation.RestController *)")
  public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
    String clase = joinPoint.getSignature().getDeclaringTypeName();
    String metodo = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    if (!clase.contains("PatientController")) {
      return joinPoint.proceed();
    }

    if (args.length == 0) {
      logger.info("[PROXY] Endpoint llamado: /{} sin argumentos", metodo);
      return joinPoint.proceed();
    }

    logger.info("[PROXY] Endpoint llamado: /{} con argumentos: {}", metodo, Arrays.toString(args));

    return joinPoint.proceed();
  }
}
```

_Observaciones_

- Se crea una clase con anotacion `@Component` para que Spring la reconozca como un componente dentro del Spring
  Container
- Dentro de la anotación `@Around` se define con `within` que se aplique a todos los métodos de las clases
  que tengan la anotación `@RestController`
- La función `logAroundController` recibe un `ProceedingJoinPoint` que permite acceder a la información del método
  invocado (como de qué clase proviene, nombre del método y parámetros)

## Patrón Builder

- El Patrón Builder consiste en que una clase puede definir sus propiedades de forma dinámica mediante un constructor
- En este caso, se implementó el patrón Builder para la clase `Patient` dentro de la capa de `model`

```java
public class Patient {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String dni;
  private String memberNumber;
  private LocalDate birthDate;
  private boolean isActive;
  private String socialSecurity;

  private Patient(PatientBuilder builder) {
    this.id = builder.id;
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.email = builder.email;
    this.phoneNumber = builder.phoneNumber;
    this.dni = builder.dni;
    this.memberNumber = builder.memberNumber;
    this.birthDate = builder.birthDate;
    this.isActive = builder.isActive;
    this.socialSecurity = builder.socialSecurity;
  }

  public static PatientBuilder builder() {
    return new PatientBuilder();
  }

  public static class PatientBuilder {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dni;
    private String memberNumber;
    private LocalDate birthDate;
    private boolean isActive;
    private String socialSecurity;

    private PatientBuilder() {
    }

    public PatientBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public PatientBuilder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public PatientBuilder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public PatientBuilder email(String email) {
      this.email = email;
      return this;
    }

    public PatientBuilder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public PatientBuilder dni(String dni) {
      this.dni = dni;
      return this;
    }

    public PatientBuilder memberNumber(String memberNumber) {
      this.memberNumber = memberNumber;
      return this;
    }

    public PatientBuilder birthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
      return this;
    }

    public PatientBuilder isActive(boolean isActive) {
      this.isActive = isActive;
      return this;
    }

    public PatientBuilder socialSecurity(String socialSecurity) {
      this.socialSecurity = socialSecurity;
      return this;
    }

    public Patient build() {
      return new Patient(this);
    }
  }
}
```
