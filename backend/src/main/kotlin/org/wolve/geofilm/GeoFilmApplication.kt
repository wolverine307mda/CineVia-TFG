package org.wolve.geofilm

import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class GeoFilmApplication {

    @Value("\${server.port}")
    private lateinit var serverPort: String

    private val logger = LoggerFactory.getLogger(GeoFilmApplication::class.java)

    // Este Bean ejecuta Flyway manualmente después de que Hibernate cree las tablas.
    @Bean
    fun flywayRunner(flyway: Flyway): ApplicationRunner {
        return ApplicationRunner {
            // Ejecutamos la migración de Flyway después de que Hibernate haya creado las tablas
            flyway.migrate()
            logger.info("Migración de Flyway ejecutada con éxito.")

            // Crear la URL con el puerto
            val urlPort = "http://localhost:$serverPort"
            val urlSwagger = "http://localhost:$serverPort/swagger-ui/index.html\n"


            // Loggear el puerto en el que la aplicación está corriendo, con un enlace
            logger.info("La aplicación está corriendo en: $urlPort")
            logger.info("Este es el enlace a Swagger: $urlSwagger")

        }
    }
}

fun main(args: Array<String>) {
    runApplication<GeoFilmApplication>(*args)
}
