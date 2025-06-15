# backend/Dockerfile
FROM openjdk:21-jdk-slim

# 1) Añadimos el repo oficial de PostgreSQL para poder instalar la v17
RUN apt-get update \
 && apt-get install -y wget gnupg2 ca-certificates \
 && echo "deb http://apt.postgresql.org/pub/repos/apt bookworm-pgdg main" \
       > /etc/apt/sources.list.d/pgdg.list \
 && wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc \
       | apt-key add - \
 && apt-get update \
 && apt-get install -y postgresql-client-17 perl \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
#RUN ./gradlew build -x test

EXPOSE 8080
CMD ["java", "-jar", "build/libs/app.jar"]
