// GeoFilmApplicationTests.kt
package org.wolve.geofilm

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Import(TestcontainersConfiguration::class)
@SpringBootTest(
    classes = [ GeoFilmApplication::class, TestcontainersConfiguration::class ],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class GeoFilmApplicationTests {
    @Test
    fun contextLoads() { /* simplemente arranca la app con Testcontainers */ }
}
