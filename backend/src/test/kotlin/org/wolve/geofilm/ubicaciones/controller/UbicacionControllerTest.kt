/*package org.wolve.geofilm.ubicaciones.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.wolve.geofilm.ubicaciones.models.Ubicacion
import org.wolve.geofilm.ubicaciones.service.IUbicacionService
import java.util.*

@WebMvcTest(UbicacionController::class)
class UbicacionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var ubicacionService: IUbicacionService

    @Test
    fun `debe devolver todas las ubicaciones`() {
        val ubicaciones = listOf(
            Ubicacion(id = "1", nombre = "Lugar A", latitud = 40.0, longitud = -3.0),
            Ubicacion(id = "2", nombre = "Lugar B", latitud = 41.0, longitud = -4.0)
        )
        every { ubicacionService.findAll() } returns ubicaciones

        val result = mockMvc.get("/api/ubicaciones")
            .andExpect { status().isOk }
            .andReturn()

        val json = result.response.contentAsString
        assert(json.contains("Lugar A"))
        assert(json.contains("Lugar B"))
    }

    @Test
    fun `debe devolver ubicacion por ID`() {
        val ubicacion = Ubicacion(id = "1", nombre = "Lugar A", latitud = 40.0, longitud = -3.0)
        every { ubicacionService.findById("1") } returns Optional.of(ubicacion)

        val result = mockMvc.get("/api/ubicaciones/1")
            .andExpect { status().isOk }
            .andReturn()

        val json = result.response.contentAsString
        assert(json.contains("Lugar A"))
    }

    @Test
    fun `debe eliminar ubicacion por ID`() {
        every { ubicacionService.deleteById("1") } returns Unit

        mockMvc.delete("/api/ubicaciones/1")
            .andExpect { status().isNoContent }

        verify(exactly = 1) { ubicacionService.deleteById("1") }
    }
}
*/