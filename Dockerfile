FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/crud-pacientes-0.0.1-SNAPSHOT.jar crud-pacientes.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "crud-pacientes.jar"]