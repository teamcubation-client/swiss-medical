# Configuración y Ejecución

Este documento explica cómo configurar y levantar el proyecto en distintos entornos usando perfiles de Spring Boot.

---

## Uso de perfiles Spring Boot

Para facilitar la configuración y evitar cambiar manualmente archivos, el proyecto utiliza perfiles de Spring Boot para manejar distintos entornos: **local** y **docker**.

### Archivos de configuración

- `application.properties`: configuración común para todos los entornos.
- `application-local.properties`: configuración para ambiente local (MySQL corriendo en tu máquina).
- `application-docker.properties`: configuración para ambiente Docker (MySQL corriendo en contenedor).

---

### Cómo activar un perfil

Spring Boot carga automáticamente `application.properties` y luego el archivo correspondiente al perfil activo, por ejemplo `application-local.properties` si el perfil es `local`.

### 1. Ejecutar en local

Para correr la app usando el perfil **local**, indicá el perfil activo al arrancar la aplicación:

#### En Maven (línea de comando):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```
####  En Intellij IDEA
- Editá la configuración de ejecución (Run Configuration) agregando el argumento de programa:
  `--spring.profiles.active=local`
### 2. Ejecutar en docker
Se pasa la varialbe para activar el perfil de la aplicación en docker-compose.yml:
```environment:
      SERVER_PORT: ${SERVER_PORT}
      SPRING_DATASOURCE_URL: "${DB_BASE_MYSQL}:${DB_PORT_CONTAINER}/${DB_DATABASE}?${DB_CONFIG_PARAMS_MYSQL}"
      SPRING_DATASOURCE_USERNAME: ${SPRING_DATASOURCE_USERNAME}
      SPRING_DATASOURCE_PASSWORD: ${SPRING_DATASOURCE_PASSWORD}
      SPRING_PROFILES_ACTIVE: docker
  ```

Esto hará que Spring Boot cargue **application-docker.properties** automáticamente.

### Archivos no subidos al repositorio
Se muestra un ejemplo vacío de los archivos con datos sensibles utilizados.

**.env**
```# .env (ejemplo)
SERVER_PORT=8080
HOST_PORT=8090
CONTAINER_PORT=8080

# DB Settings
DB_PORT_CONTAINER=3306
DB_PORT_HOST=3307
DB_BASE_MYSQL=jdbc:mysql://localhost
DB_DATABASE=api_pacientes
DB_CONFIG_PARAMS_MYSQL=useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC

# Credenciales (sólo de ejemplo, cambiar por reales)
SPRING_DATASOURCE_USERNAME=tu_usuario
SPRING_DATASOURCE_PASSWORD=tu_contraseña
MYSQL_ROOT_PASSWORD=tu_root_password
```
**application-local.properties**
```# application-local.properties
spring.datasource.url=jdbc:mysql://localhost:3306/api_pacientes?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```
