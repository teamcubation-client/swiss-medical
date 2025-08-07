# 1. Build de la aplicación Java + Spring Boot
FROM maven:3-amazoncorretto-17-debian AS build

WORKDIR /app

# Copiar el pom.xml y el código fuente
COPY pom.xml .

COPY src ./src

COPY .env ./.env

# Construir la aplicación
RUN mvn clean package -DskipTests

# 2. Crear la imagen final con la aplicación
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar
# Copiar el archivo .env desde la etapa de construcción
COPY --from=build /app/.env ./.env

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
