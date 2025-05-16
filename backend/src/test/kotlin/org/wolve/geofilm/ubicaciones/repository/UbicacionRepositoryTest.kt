/*package org.wolve.geofilm.ubicaciones.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.wolve.geofilm.ubicaciones.models.Ubicacion

@DataJpaTest
class UbicacionRepositoryTest @Autowired constructor(
    val ubicacionRepository: UbicacionRepository
) {

    @Test
    @DisplayName("Debe guardar y recuperar una ubicación correctamente")
    fun testGuardarYRecuperarUbicacion() {
        val ubicacion = Ubicacion(
            nombre = "Parque Central",
            latitud = 40.7128,
            longitud = -74.0060
        )

        val saved = ubicacionRepository.save(ubicacion)
        val found = ubicacionRepository.findById(saved.id)

        assertTrue(found.isPresent)
        assertEquals("Parque Central", found.get().nombre)
    }

    @Test
    @DisplayName("Debe devolver una lista vacía cuando no hay ubicaciones")
    fun testBuscarUbicacionesVacio() {
        val ubicaciones = ubicacionRepository.findAll()
        assertTrue(ubicaciones.isEmpty())
    }
}*/