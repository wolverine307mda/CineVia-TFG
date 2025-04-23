package org.wolve.geofilm

import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
class GeoFilmApplication {

    @Value("\${server.port}")
    private lateinit var serverPort: String

    private val logger = LoggerFactory.getLogger(GeoFilmApplication::class.java)

    // Bean manual para configurar Flyway (porque spring.flyway.enabled=false)
    @Bean
    fun flyway(dataSource: DataSource): Flyway {
        return Flyway.configure()
            .dataSource(dataSource) // Usa la misma conexión de base de datos que Spring Boot
            .locations("classpath:db/migration") // Ruta donde tienes tus scripts SQL de Flyway
            .baselineOnMigrate(true) // Crea una baseline si es necesario (evita errores en BD existentes)
            .load()
    }

    // Este Bean se ejecuta al arrancar la aplicación
    // y lanza las migraciones de Flyway después de que Hibernate haya creado las tablas
    @Bean
    fun flywayRunner(flyway: Flyway): ApplicationRunner {
        return ApplicationRunner {
            // Ejecuta las migraciones de Flyway
            flyway.migrate()
            logger.info("✅ Migración de Flyway ejecutada con éxito.")

            // Mostrar en consola la URL base de la app
            val urlPort = "http://localhost:$serverPort"
            val urlSwagger = "http://localhost:$serverPort/swagger-ui/index.html\n"

            logger.info("🚀 La aplicación está corriendo en: $urlPort")
            logger.info("🔍 Swagger disponible en: $urlSwagger")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<GeoFilmApplication>(*args)
}
