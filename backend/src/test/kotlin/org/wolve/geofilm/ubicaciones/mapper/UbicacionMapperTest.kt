/*package org.wolve.geofilm.ubicaciones.mapper

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.wolve.geofilm.ubicaciones.models.Ubicacion
import org.wolve.geofilm.producciones.produccion.models.Produccion

class UbicacionMapperTest {

    private val mapper = UbicacionMapper()

    @Test
    fun `should map entity to dto`() {
        val produccion = Produccion(id = "prod1")
        val ubicacion = Ubicacion(
            id = "ub1",
            nombre = "Plaza Mayor",
            latitud = 40.0,
            longitud = -3.7,
            produccion = produccion
        )

        val dto = mapper.toDTO(ubicacion)

        assertEquals("ub1", dto.id)
        assertEquals("Plaza Mayor", dto.nombre)
        assertEquals(40.0, dto.latitud)
        assertEquals(-3.7, dto.longitud)
        assertEquals("prod1", dto.produccionId)
    }

    @Test
    fun `should map dto to entity`() {
        val dto = UbicacionDTO(
            id = "ub1",
            nombre = "Plaza Mayor",
            latitud = 40.0,
            longitud = -3.7,
            produccionId = "prod1"
        )

        val ubicacion = mapper.toEntity(dto)

        assertEquals("ub1", ubicacion.id)
        assertEquals("Plaza Mayor", ubicacion.nombre)
        assertEquals(40.0, ubicacion.latitud)
        assertEquals(-3.7, ubicacion.longitud)
        assertEquals("prod1", ubicacion.produccion?.id)
    }

    @Test
    fun `should return null when input is null`() {
        assertNull(mapper.toDTO(null))
        assertNull(mapper.toEntity(null))
    }
}*/