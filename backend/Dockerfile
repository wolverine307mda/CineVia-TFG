# backend/Dockerfile
FROM openjdk:21-jdk-slim

# Instala cliente de PostgreSQL
RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/*

# Establece el directorio de trabajo
WORKDIR /app

# Copia todos los archivos del backend, incluido gradlew
COPY . .

# Da permisos de ejecución al wrapper de Gradle
RUN chmod +x ./gradlew

# Compila el proyecto sin ejecutar tests
#RUN ./gradlew build -x test

# Expone el puerto del backend
EXPOSE 8080

# Comando por defecto al iniciar el contenedor
CMD ["java", "-jar", "build/libs/app.jar"]
