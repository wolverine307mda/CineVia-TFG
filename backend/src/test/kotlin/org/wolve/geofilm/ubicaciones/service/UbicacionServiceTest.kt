/*
package org.wolve.geofilm.ubicaciones.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.wolve.geofilm.ubicaciones.models.Ubicacion
import org.wolve.geofilm.ubicaciones.repository.UbicacionRepository
import java.util.*

class UbicacionServiceTest {

    private lateinit var ubicacionService: IUbicacionService
    private val ubicacionRepository: UbicacionRepository = mockk()

    @Test
    fun `test findAll returns list of ubicaciones`() {
        val mockUbicaciones = listOf(
            Ubicacion(id = "1", nombre = "Ubicacion 1", latitud = 40.0, longitud = -3.0),
            Ubicacion(id = "2", nombre = "Ubicacion 2", latitud = 41.0, longitud = -4.0)
        )
        every { ubicacionRepository.findAll() } returns mockUbicaciones

        val result = ubicacionService.getAllUbicaciones()
        assertEquals(2, result.size)
        verify { ubicacionRepository.findAll() }
    }

    @Test
    fun `test findById returns correct ubicacion`() {
        val ubicacion = Ubicacion(id = "1", nombre = "Test", latitud = 10.0, longitud = 20.0)
        every { ubicacionRepository.findById("1") } returns Optional.of(ubicacion)

        val result = ubicacionService.getUbicacionById("1")
        assertEquals("Test", result?.nombre)
        verify { ubicacionRepository.findById("1") }
    }

    /*@Test
    fun `test save stores ubicacion`() {
        val ubicacion = Ubicacion(id = "1", nombre = "New", latitud = 10.0, longitud = 20.0)
        every { ubicacionRepository.save(ubicacion) } returns ubicacion

        val result = ubicacionService.createUbicacion(ubicacion)
        assertEquals("New", result.nombre)
        verify { ubicacionRepository.save(ubicacion) }
    }*/

    @Test
    fun `test deleteById calls repository`() {
        every { ubicacionRepository.deleteById("1") } returns Unit

        ubicacionService.deleteUbicacion("1")
        verify { ubicacionRepository.deleteById("1") }
    }
}
*/