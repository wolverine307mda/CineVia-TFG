FROM openjdk:21-jdk-slim
WORKDIR /app

# Copia el .jar generado localmente
COPY build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]