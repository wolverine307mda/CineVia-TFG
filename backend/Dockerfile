# Etapa 1: Construcción
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

# Compilar usando Spring Boot (esto genera app.jar en build/libs)
RUN gradle bootJar --no-daemon

# Etapa 2: Imagen final con OpenJDK
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copiamos el jar renombrado como app.jar
COPY --from=build /app/build/libs/app.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
