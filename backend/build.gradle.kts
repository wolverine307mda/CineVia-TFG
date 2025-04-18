plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.noarg") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.0"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("org.flywaydb.flyway") version "10.11.1"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
    // 📌 Spring Boot Core
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-graphql")

    // 📌 Persistencia y Base de Datos
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA + Hibernate
    implementation("org.postgresql:postgresql") // Driver de PostgreSQL
    implementation("org.flywaydb:flyway-core:10.11.1")
    implementation("org.flywaydb:flyway-database-postgresql:10.11.1")

    // 📌 Validaciones
    implementation("org.springframework.boot:spring-boot-starter-validation") // Validaciones de DTOs

    // 📌 Cache (Opcional, mejora rendimiento)
    implementation("org.springframework.boot:spring-boot-starter-cache") // Caching con Redis u otros

    // 📌 GraphQL
    implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:12.0.0") // GraphQL Avanzado
    implementation("com.graphql-java-kickstart:graphql-java-tools:12.0.0") // Resolver automático de GraphQL

    // 📌 Conversión de Datos (DTO ↔ Entity)
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    // 📌 JSON y Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // 📌 Documentación API con OpenAPI (Swagger)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    // 📌 Logs avanzados
    implementation("ch.qos.logback:logback-classic")

    // 📌 Sesión y Autenticación
    implementation("org.springframework.session:spring-session-core")
    implementation("com.auth0:java-jwt:4.4.0")

    // 📌 Lombok (Opcional, reduce código)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Redis (Caché)
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("io.lettuce:lettuce-core")

    implementation("org.springframework.boot:spring-boot-starter-security")

    // 📌 Test y Contenedores
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveFileName.set("app.jar")
}

tasks.test {
    outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
    inputs.dir(project.extra["snippetsDir"]!!)
    dependsOn(tasks.test)
}