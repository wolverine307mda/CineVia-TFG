import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.noarg") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.25"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("org.flywaydb.flyway") version "10.11.1"
    id("jacoco")
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

    // 📌 Persistencia y Base de Datos
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core:10.11.1")
    implementation("org.flywaydb:flyway-database-postgresql:10.11.1")

    // 📌 Validaciones
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // 📌 Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // 📌 Conversión de Datos (DTO ↔ Entity)
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    // 📌 JSON y Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // 📌 Documentación API con OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    // 📌 Logs
    implementation("ch.qos.logback:logback-classic")
    testRuntimeOnly("org.apache.logging.log4j:log4j-core:2.23.1")

    // 📌 Sesión y JWT
    implementation("org.springframework.session:spring-session-core")
    implementation("com.auth0:java-jwt:4.4.0")

    // 📌 Lombok (Opcional)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // 📌 Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("io.lettuce:lettuce-core")

    // 📌 Envio de emails
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // 📌 Firebase
    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation("com.google.cloud:google-cloud-storage:2.27.0")

    // 📌 Test y Contenedores
    testImplementation("com.ninja-squad:springmockk:4.0.0")
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

    // ✅ Mockito y MapStruct para tests
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.mockk:mockk:1.13.10")
}

tasks.withType<BootJar> {
    archiveFileName.set("app.jar")
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.test {
    useJUnitPlatform()
    systemProperties(
        "spring.autoconfigure.exclude" to listOf(
            "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
            "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration"
        ).joinToString(",")
    )
    outputs.dir(project.extra["snippetsDir"]!!)

    include("**/*Test.*", "**/*Tests.*")
    // Después de ejecutar `test`, dispara el reporte de JaCoCo:
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)    // Genera HTML (con colores / semáforos)
        xml.required.set(true)     // Genera XML (útil para CI o SonarQube)
        csv.required.set(false)    // Normalmente no hace falta CSV
    }

    // Indica dónde están tus ficheros fuente, para que el HTML pinte las líneas correctamente:
    sourceDirectories.setFrom(files("src/main/kotlin", "src/main/java"))

    // Indica dónde buscar las clases compiladas (para calcular cobertura):
    classDirectories.setFrom(
        fileTree("$buildDir/classes/kotlin/main") {
            include("**/*.class")
            // Si quieres excluir clases generadas (por ejemplo, clases synthetic de Kotlin),
            // puedes añadir aquí exclude("**/*$DefaultImpls.class") o similares.
        },
        fileTree("$buildDir/classes/java/main") {
            include("**/*.class")
        }
    )

    // Indica dónde JaCoCo vuelca el fichero .exec con los datos de cobertura:
    executionData.setFrom(
        fileTree(buildDir) {
            include("**/jacoco/*.exec")
            include("**/jacocoTest.exec")
        }
    )
}

tasks.asciidoctor {
    inputs.dir(project.extra["snippetsDir"]!!)
    dependsOn(tasks.test)
}