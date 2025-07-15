# Etapa 1: Build - compilar y empaquetar la aplicación
# Toma imagen con maven y java 11
FROM maven:3.9.10-eclipse-temurin-11 AS build
# Definir el directorio de trabajo dentro del contenedor
WORKDIR /app
# Copiar todo el código fuente al contenedor
COPY . .
# Ejecutar Maven para limpiar y empaquetar sin correr tests
RUN mvn clean package -DskipTests


# Etapa 2: Runtime
# Imagen liviana para ejecutar la aplicación
FROM eclipse-temurin:11-jre
# Definir directorio de trabajo para runtime
WORKDIR /app
# Copiar el archivo .jar generado en la etapa de build
# Renombrarlo como app.jar para evitar tener que cambiarlo si se modifica la versión en el pom
COPY --from=build /app/target/*.jar app.jar
# Copiar script y darle permisos (me aseguro que la base de datos haya terminado de levantar primero)
COPY wait-for.sh .
RUN chmod +x wait-for.sh
# Detalla que puerto utiliza el proyecto (a modo documentacion)
EXPOSE 8080
# ENTRYPOINT en formato "exec form":
# Define el comando para ejecutar el proyecto
ENTRYPOINT ["./wait-for.sh", "db_pacientes", "3306", "java", "-jar", "app.jar"]